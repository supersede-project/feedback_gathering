package ch.uzh.supersede.feedbacklibrary.models;

/**
 * Created by Skidan Oleg on 25.06.2017.
 */

public class EditImageItem {
    private String title;
    private int elementResource;
    private OnClickListener listener;

    public EditImageItem() {
    }

    public EditImageItem(String title, int elementResource) {
        this.title = title;
        this.elementResource = elementResource;
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

    public void setOnClickListener(OnClickListener onClickListener) {
        listener = onClickListener;
    }

    public void click() {
        listener.onClick();
    }

    public interface OnClickListener {
        void onClick();
    }
}
