package models.StoryState;

public class StoryStateView {
    public long id;
    public boolean inProgress;
    public int dev;
    public int cr;
    public int qa;

    public StoryStateView(long id, boolean inProgress, int dev, int cr, int qa) {
        this.id = id;
        this.inProgress = inProgress;
        this.dev = dev;
        this.cr = cr;
        this.qa = qa;
    }
}
