package ch.uzh.supersede.feedbacklibrary.models;

public final class EditImageItem {
    private String title;
    private int elementResource;
    private OnClickListener listener;

    public EditImageItem() {
    }

    public EditImageItem(String title, int elementResource) {
        this.title = title;
        this.elementResource = elementResource;
    }

    public interface OnClickListener {
        void onClick();
    }

    public void click() {
        listener.onClick();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        listener = onClickListener;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getElementResource() {
        return elementResource;
    }

    public void setElementResource(int elementResource) {
        this.elementResource = elementResource;
    }

    public OnClickListener getListener() {
        return listener;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }
}
