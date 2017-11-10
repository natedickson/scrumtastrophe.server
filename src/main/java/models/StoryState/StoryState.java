package models.StoryState;

public class StoryState {
    public long id;
//    public String title;
    public boolean inProgress;
    public int dev;
    public int cr;
    public int qa;

    public StoryState(long id, String title, int dev, int qa) {
        this.id = id;
//        this.title = title;
        this.inProgress = false;
        this.dev = dev;
        this.cr = 2; //magic number
        this.qa = qa;
    }

    public StoryStateView getStoryStateView() {
        return new StoryStateView(id, inProgress, dev, cr, qa);
    }

    public String getProgress() {
        if(dev > 0) return "dev";
        if(cr > 0) return "cr";
        if(qa > 0) return "qa";
        return "complete";
    }
}
