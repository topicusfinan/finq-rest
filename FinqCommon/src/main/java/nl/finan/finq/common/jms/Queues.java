package nl.finan.finq.common.jms;

public final class Queues
{

    private Queues()
    {
    }

    public static final String RUN_STORY_QUEUE = "java:/jms/runStoriesQueue";
    public static final String STATUS_QUEUE = "java:/jms/statusQueue";
    public static final String CONNECTION_FACTORY = "java:/ConnectionFactory";
}
