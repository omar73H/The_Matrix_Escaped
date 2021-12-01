package code;

class LotsOfBooleans
{
    String[] a0;
    String[] a1;
    
    public LotsOfBooleans() {
		a0 = new String[200];
		a1 = new String[200];
		for(int i=0;i<200;i++)
			a0[i] = "a"+0;
		for(int i=0;i<200;i++)
			a1[i] = "a"+1;
	}
}

class LotsOfInts
{
    int[] a0;
    public LotsOfInts() {
		a0 = new int[200];
	}
}


public class Test
{
    private static final int SIZE = 10000;

    public static void main(String[] args) throws Exception
    {        
    	
        LotsOfBooleans[] first = new LotsOfBooleans[SIZE];
        LotsOfInts[] second = new LotsOfInts[SIZE];

        System.gc();
        long startMem = getMemory();

        for (int i=0; i < SIZE; i++)
        {
            first[i] = new LotsOfBooleans();
        }

        System.gc();
        long endMem = getMemory();

        System.out.println ("Size for LotsOfBooleans: " + (endMem-startMem));
        System.out.println ("Average size: " + ((endMem-startMem) / ((double)SIZE)));

        System.gc();
        startMem = getMemory();
        for (int i=0; i < SIZE; i++)
        {
            second[i] = new LotsOfInts();
        }
        System.gc();
        endMem = getMemory();

        System.out.println ("Size for LotsOfInts: " + (endMem-startMem));
        System.out.println ("Average size: " + ((endMem-startMem) / ((double)SIZE)));

        // Make sure nothing gets collected
        long total = 0;
        for (int i=0; i < SIZE; i++)
        {
            total += (first[i].a0[0]==""?1:0) + second[i].a0[0];
        }
        System.out.println(total);
    }

    private static long getMemory()
    {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}