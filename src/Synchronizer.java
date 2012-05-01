import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;


public class Synchronizer
{
    private static long keys = 0;
    
    private Synchronizer() {}
    
    public static Synchronizer.Key getNewKey(Class<?>[] types)
    {
        return new Synchronizer.Key(keys++, types);
    }
    public static void synchronize(Synchable s) //Threadsafe, but terrible. Should synch on Keys, not Synchables
    {
        
        (new Synchronizer.RunnableSynch(s)).run();
    }
    
    public static class Key
    {
        private final long key;
        private final Class<?>[] types;
        private final HashSet<Synchable> set;
        private final ReentrantLock lock = new ReentrantLock();
        private Key()
        {
            types = null;
            set = new HashSet<Synchable>();
            key = keys++;
        }
        private Key(long key, Class<?>[] types)
        {
            this.types = types.clone();
            set = new HashSet<Synchable>();
            this.key = key;
        }
        public int hashCode()
        {
            return (new Long(key)).hashCode();
        }
        public void add(Synchable s)
        {
            set.add(s);
        }
        public boolean remove(Synchable s)
        {
            return set.remove( s );
        }
    }
    public static interface Synchable
    {
        public static enum Index {
            ;
            public final Class<?> type;
            Index (Class<?> type)
            {
                this.type = type;
            }
            
            private static Class<?>[] getIndexTypes()
            {
                Class<?>[] ret = new Class<?>[Index.values().length];
                for(int i = 0; i < Index.values().length; i++)
                {
                    ret[i] = Index.values()[i].type;
                }
                return ret;
            }
        }
        public abstract Synchronizer.Key[] getKeys();
        public abstract void push(HashMap<Synchronizer.Key, Object[]> map);
        public abstract HashMap<Synchronizer.Key, Object[]> pull();
    }
    private static class RunnableSynch implements Runnable
    {
        private final Synchronizer.Synchable synch;
        
        private RunnableSynch(Synchronizer.Synchable synch)
        {
            this.synch = synch;
        }
        
        @Override
        public void run()
        {
            Synchronizer.Key[] keys = synch.getKeys();
            for(int i = 0; i < keys.length; i++)
            {
                keys[i].lock.lock();
                try{
                    HashMap<Synchronizer.Key, Object[]> map = synch.pull();
                    Object[] values = map.get( keys[i] );
                    if(values == null)
                    {
                        return;
                    }
                    for(int j = 0; j < keys[i].types.length; j++)
                    {
                        if(values[j] == null)
                        {
                            continue;
                        }
                        if( ! keys[i].types[j].isAssignableFrom( values[j].getClass() ))
                        {
                            throw new ClassCastException(
                                "The specified type " + keys[i].types[j] + ", at index " +
                                j + ", is not applicable to the class " +  
                                values[j].getClass() + ".");
                        }
                    }
                    for( Synchable toSynch : keys[i].set)
                    {
                        toSynch.push( map );
                    }
                }
                finally
                {
                    keys[i].lock.unlock();
                }
            }
           
        }
        
    }
}
