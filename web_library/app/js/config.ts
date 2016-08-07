export const apiEndpoint = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/';
export const configPath = 'feedback_orchestrator/example/configuration';
export const feedbackPath = 'feedback_repository/example/feedback';

export const feedbackObjectTitle = 'Feedback';
export const applicationName = 'energiesparkonto.de';

export const defaultSuccessMessage = 'Your feedback was successfully sent';

export const dialogOptions = {
    autoOpen: false,
    height: 'auto',
    width: 'auto',
    minWidth: 500,
    modal: true,
    title: 'Feedback',
    buttons: {},
    resizable: false
};

export const mechanismTypes = {
    textType: 'TEXT_TYPE',
    ratingType: 'RATING_TYPE',
    screenshotType: 'SCREENSHOT_TYPE',
    audioType: 'AUDIO_TYPE',
    categoryType: 'CATEGORY_TYPE',
    attachmentType: 'ATTACHMENT_TYPE'
};

export const configurationTypes = {
    push: 'PUSH',
    pull: 'PULL'
};
