import {AttachmentFeedback} from './attachment_feedback';


describe('Attachment feedback', () => {

    it('should return a file extension for a content type', () => {
        expect(new AttachmentFeedback('', '', 'application/pdf', null).fileExtension).toEqual('pdf');
        expect(new AttachmentFeedback('', '', 'application/xxx', null).fileExtension).toEqual('xxx');
        expect(new AttachmentFeedback('', '', 'text/plain', null).fileExtension).toEqual('txt');
        expect(new AttachmentFeedback('', '', 'text', null).fileExtension).toEqual('');
    });
});

