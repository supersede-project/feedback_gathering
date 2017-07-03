import {I18nHelper} from './i18n';


describe('i18n Helper', () => {

    //assumes that de and en are available, but ru not
    it('should determine the correct language depending on fallback and set language options', (done) => {
        let optionsBothAvailable = {
            'fallbackLang': 'de',
            'lang': 'en',
            'distPath': 'base/app/'
        };
        let langNotAvailable = {
            'fallbackLang': 'de',
            'lang': 'ru',
            'distPath': 'base/app/'
        };

        I18nHelper.getLanguage(optionsBothAvailable, language => {
            expect(language).toEqual('en');
        });

        I18nHelper.getLanguage(langNotAvailable, language => {
            expect(language).toEqual('de');
            done();
        });
    });

    it('should override the default language object', () => {
        let data = {
            "general": {
                "review_feedback": "weiter",
                "review_feedback_title": "Dein Feedback",
                "feedback_submit_button_title": "Feedback abschicken",
                "mandatory_default_text": "Bitte Textfeld ausfüllen",
                "back": "zurück",
                "validation_max_length_error_message": "Die maximale Länge des Feldes ist {{ maxLength }}. Sie haben {{ currentLength }} Zeichen eingegeben.",
                "validation_invalid_email": "E-Mail-Adresse ungültig",
                "discard_feedback": "Abbrechen",
                "cancel": "Abbrechen",
                "dialog_close_button_title": "Minimieren",
                "success_message": "Ihr Feedback wurde erfolgreich abgeschickt.",
                "error_message": "Beim Speichern des Feedbacks ist ein Fehler aufgetreten."
            },
            "text": {
                "text_type_text_clear": "Texteingabe löschen"
            },
            "screenshot": {
                "take_screenshot": "Screenshot erstellen",
                "take_new_screenshot": "Neuen Screenshot erstellen",
                "actions": "Aktionen",
                "stickers": "Sticker",
                "rectangle": "Rechteck",
                "blacken": "Schwärzen",
                "circle": "Kreis",
                "arrow": "Pfeil",
                "freehand": "Freihand",
                "crop": "Ausschneiden",
                "text": "Text hinzufügen",
                "undo": "Rückgängig",
                "remove": "Screenshot entfernen",
                "color": "Farbe",
                "selection": "Auswahl"
            },
            "rating": {
                "review_title": "Rating"
            }
        };
        let localesOverride = {
            "general": {
                "review_feedback": "weiter",
                "error_message": "Fehler beim Speichern",
                "page_2" : {
                    "title": "2. Seite",
                    "background": "#00ff00"
                }
            },
            "text": {
                "text_type_text_clear": "löschen"
            },
            "screenshot": {
                "take_screenshot": "Screenshot",
                "take_new_screenshot": "Neu",
                "actions": "Werkzeuge",
                "stickers": "Sticker"
            }
        };

        let result = I18nHelper.overrideLocales(data, localesOverride);

        let expectedTranslationObject = {
            "general": {
                "review_feedback": "weiter",
                "review_feedback_title": "Dein Feedback",
                "feedback_submit_button_title": "Feedback abschicken",
                "mandatory_default_text": "Bitte Textfeld ausfüllen",
                "back": "zurück",
                "validation_max_length_error_message": "Die maximale Länge des Feldes ist {{ maxLength }}. Sie haben {{ currentLength }} Zeichen eingegeben.",
                "validation_invalid_email": "E-Mail-Adresse ungültig",
                "discard_feedback": "Abbrechen",
                "cancel": "Abbrechen",
                "dialog_close_button_title": "Minimieren",
                "success_message": "Ihr Feedback wurde erfolgreich abgeschickt.",
                "error_message": "Fehler beim Speichern",
                "page_2" : {
                    "title": "2. Seite",
                    "background": "#00ff00"
                }
            },
            "text": {
                "text_type_text_clear": "löschen"
            },
            "screenshot": {
                "take_screenshot": "Screenshot",
                "take_new_screenshot": "Neu",
                "actions": "Werkzeuge",
                "stickers": "Sticker",
                "rectangle": "Rechteck",
                "blacken": "Schwärzen",
                "circle": "Kreis",
                "arrow": "Pfeil",
                "freehand": "Freihand",
                "crop": "Ausschneiden",
                "text": "Text hinzufügen",
                "undo": "Rückgängig",
                "remove": "Screenshot entfernen",
                "color": "Farbe",
                "selection": "Auswahl"
            },
            "rating": {
                "review_title": "Rating"
            }
        };
        expect(result).toEqual(expectedTranslationObject);
    });
});