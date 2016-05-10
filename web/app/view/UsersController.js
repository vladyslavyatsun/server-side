/*
 * Copyright 2015 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

Ext.define('Traccar.view.UsersController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.users',

    requires: [
        'Traccar.view.UserDialog',
        'Traccar.view.UserDevices',
        'Traccar.view.UserGroups',
        'Traccar.view.BaseWindow'
    ],

    init: function () {
        Ext.getStore('Users').load();
    },

    onAddClick: function () {
        var user, dialog;
        user = Ext.create('Traccar.model.User');
        dialog = Ext.create('Traccar.view.UserDialog');
        dialog.down('form').loadRecord(user);
        dialog.show();
    },

    onEditClick: function () {
        var user, dialog;
        user = this.getView().getSelectionModel().getSelection()[0];
        dialog = Ext.create('Traccar.view.UserDialog');
        dialog.down('form').loadRecord(user);
        dialog.show();
    },

    onRemoveClick: function () {
        var user = this.getView().getSelectionModel().getSelection()[0];
        Ext.Msg.show({
            title: Strings.settingsUser,
            message: Strings.sharedRemoveConfirm,
            buttons: Ext.Msg.YESNO,
            buttonText: {
                yes: Strings.sharedRemove,
                no: Strings.sharedCancel
            },
            fn: function (btn) {
                var store = Ext.getStore('Users');
                if (btn === 'yes') {
                    store.remove(user);
                    store.sync();
                }
            }
        });
    },

    onDevicesClick: function () {
        var user = this.getView().getSelectionModel().getSelection()[0];
        Ext.create('Traccar.view.BaseWindow', {
            title: Strings.deviceTitle,
            items: {
                xtype: 'userDevicesView',
                user: user
            }
        }).show();
    },

    onGroupsClick: function () {
        var user = this.getView().getSelectionModel().getSelection()[0];
        Ext.create('Traccar.view.BaseWindow', {
            title: Strings.settingsGroups,
            items: {
                xtype: 'userGroupsView',
                user: user
            }
        }).show();
    },

    onSelectionChange: function (selected) {
        var disabled = selected.length > 0;
        this.lookupReference('toolbarEditButton').setDisabled(disabled);
        this.lookupReference('toolbarRemoveButton').setDisabled(disabled);
        this.lookupReference('userDevicesButton').setDisabled(disabled);
        this.lookupReference('userGroupsButton').setDisabled(disabled);
    }
});
