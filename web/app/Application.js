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

Ext.define('Traccar.Application', {
    extend: 'Ext.app.Application',
    name: 'Traccar',

    requires: [
        'Traccar.Style',
        'Traccar.AttributeFormatter'
    ],

    models: [
        'Server',
        'User',
        'Group',
        'Device',
        'Position',
        'Attribute',
        'Command'
    ],

    stores: [
        'Groups',
        'Devices',
        'AllGroups',
        'AllDevices',
        'Positions',
        'LatestPositions',
        'Users',
        'Attributes',
        'MapTypes',
        'DistanceUnits',
        'SpeedUnits',
        'CommandTypes',
        'TimeUnits',
        'Languages'
    ],

    controllers: [
        'Root'
    ],

    setUser: function (data) {
        var reader = Ext.create('Ext.data.reader.Json', {
            model: 'Traccar.model.User'
        });
        this.user = reader.readRecords(data).getRecords()[0];
    },

    getUser: function () {
        return this.user;
    },

    setServer: function (data) {
        var reader = Ext.create('Ext.data.reader.Json', {
            model: 'Traccar.model.Server'
        });
        this.server = reader.readRecords(data).getRecords()[0];
    },

    getServer: function () {
        return this.server;
    },

    getPreference: function (key, defaultValue) {
        return this.getUser().get(key) || this.getServer().get(key) || defaultValue;
    },

    showError: function (response) {
        var data;
        if (Ext.isString(response)) {
            Ext.Msg.alert(Strings.errorTitle, response);
        } else if (response.responseText) {
            data = Ext.decode(response.responseText);
            if (data.details) {
                Ext.Msg.alert(Strings.errorTitle, data.details);
            } else {
                Ext.Msg.alert(Strings.errorTitle, data.message);
            }
        } else if (response.statusText) {
            Ext.Msg.alert(Strings.errorTitle, response.statusText);
        } else {
            Ext.Msg.alert(Strings.errorTitle, Strings.errorConnection);
        }
    }
});
