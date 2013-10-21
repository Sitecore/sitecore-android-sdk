scmobile.contacts = function () {

    function parseBirthday(birthday) {
        try {
            if (birthday != undefined) {
                if (birthday.constructor === String) {
                    return new Date(birthday);
                } else if (birthday.constructor === Date) {
                    return birthday;
                }
            }
        } catch (exception) {
        }
    }

    var Contact = function (properties) {
        this.plugin = new scmobile.utils.ScPlugin('contacts');
        if (properties !== undefined) {
            this.firstName = properties.firstName;
            this.lastName = properties.lastName;
            this.company = properties.company;
            this.emails = properties.emails;
            this.phones = properties.phones;
            this.websites = properties.websites;
            this.photo = properties.photo;
            this.birthday = parseBirthday(properties.birthday);
            this.addresses = properties.addresses;
        }

    }

    /**
     * Show pre-filled contact form
     */
    Contact.prototype.save = function (onSuccess, onError) {
        var params = {
            firstName: this.firstName,
            lastName: this.lastName,
            company: this.company,
            emails: this.emails,
            phones: this.phones,
            websites: this.websites,
            photo: this.photo,
            birthday: parseBirthday(this.birthday),
            addresses: this.addresses
        }

        this.plugin.exec("save", params, onSuccess, onError);
    }

    /**
     * Save contact data without user interaction.
     */
    Contact.prototype.silentSave = function (onSuccess, onError) {
        var params = {
            firstName: this.firstName,
            lastName: this.lastName,
            company: this.company,
            emails: this.emails,
            phones: this.phones,
            websites: this.websites,
            photo: this.photo,
            birthday: parseBirthday(this.birthday),
            addresses: this.addresses,
            id: this.id,
            rawId: this.rawId
        }

        this.plugin.exec("silentSave", params, onSuccess, onError);
    }

    /**
     * Delete contact without user interaction.
     */
    Contact.prototype.remove = function (onSuccess, onError) {
        var params = {
            id: this.id
        }

        this.plugin.exec("remove", params, onSuccess, onError);
    }

    return {
        /**
         *
         */
        create: function (properties) {
            return new Contact(properties);
        },

        /**
         * Get all contacts and filter on js side
         */
        silentSelect: function (predicate, onSuccess, onError) {
            var plugin = new scmobile.utils.ScPlugin('contacts');

            function onLoadFinished(data) {
                var contacts = JSON.parse(data);
                var contactsResult = [];
                if (contacts) {
                    for (var i = 0; i < contacts.length; i++) {
                        var contact = new Contact(contacts[i]);
                        contact.id = contacts[i].id;
                        contact.rawId = contacts[i].rawId;
                        if (typeof predicate === 'undefined' || predicate(contact)) {
                            contactsResult.push(contact);
                        }
                    }
                }

                onSuccess(contactsResult);
            }

            plugin.exec("select", {}, onLoadFinished, onError);
        },

        /**
         * Removes list of contacts. More efficient than deleting one by one.
         * @param contactIds JSON array of contact ids
         * @param onSuccess
         * @param onError
         */
        removeMultiple: function (contactIds, onSuccess, onError) {
            var plugin = new scmobile.utils.ScPlugin('contacts');
            var params = {
                ids: contactIds || []
            }
            plugin.exec("removeMultiple", params, onSuccess, onError);
        }
    }
}();
