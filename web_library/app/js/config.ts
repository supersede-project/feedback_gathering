export const apiEndpointOrchestrator = 'https://ptvfeedback.ronnieschaniel.com/';
export const applicationPath = 'feedback_orchestrator/{lang}/applications/';
export const applicationId = 8;

export const apiEndpointRepository = 'https://ptvfeedback.ronnieschaniel.com/';
export const feedbackPath = 'feedback_repository/{lang}/feedbacks';

export const feedbackObjectTitle = 'Feedback';
export const applicationName = 'PTV';

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

export const cookieNames = {
  lastTriggered: 'lastTriggered'
};