package ch.uzh.supersede.feedbacklibrary.components.views;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.models.AbstractFeedbackPart;
import ch.uzh.supersede.feedbacklibrary.models.TextFeedback;

public class TextMechanismView extends AbstractFeedbackPartView {
    private TextFeedback textFeedback ;

    public TextMechanismView(LayoutInflater layoutInflater, AbstractFeedbackPart mechanism) {
        super(layoutInflater);
        this.viewOrder = mechanism.getOrder();
        this.textFeedback = (TextFeedback) mechanism;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.mechanism_text_enclosing, null));
        initView();
    }

    private void initView() {
        final TextInputLayout textInputLayout = (TextInputLayout) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_input_layout);
        final TextInputEditText textInputEditText = (TextInputEditText) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_text);

        // Set the hint and enable it
        textInputLayout.setHintEnabled(true);
        textInputLayout.setHint(textFeedback.getHint());

        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //nop
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    textInputLayout.setHint(textFeedback.getLabel());
                } else if (s.length() == 0) {
                    textInputLayout.setHint(textFeedback.getHint());
                }

                if (textFeedback.getMaxLength() != null && textInputLayout.isErrorEnabled()) {
                    if (s.length() > textFeedback.getMaxLength()) {
                        textInputLayout.setError(getEnclosingLayout().getResources().getString(R.string.feedback_text_warning, textFeedback.getMaxLength()));
                    } else {
                        textInputLayout.setError(null);
                    }
                }
            }
        });
    }

    @Override
    public void updateModel() {
        textFeedback.setText(((TextInputEditText) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_text)).getText().toString());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof AbstractFeedbackPartView) {
            int comparedViewOrder = ((AbstractFeedbackPartView) o).getViewOrder();
            return comparedViewOrder > getViewOrder() ? -1 : comparedViewOrder == getViewOrder() ? 0 : 1;
        }
        return 0;
    }
}
