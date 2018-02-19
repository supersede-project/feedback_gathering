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
    attachmentType: 'ATTACHMENT_TYPE',
    infoType: 'INFO_TYPE'
};
export const configurationTypes = {
    push: 'PUSH',
    pull: 'PULL',
    elementSpecificPush: 'ELEMENT_SPECIFIC_PUSH'
};
export const cookieNames = {
    lastTriggered: 'lastTriggered',
};