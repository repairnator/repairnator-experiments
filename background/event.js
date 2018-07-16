
// Unify messaging method - And eliminate callbacks (a message is replied with another message instead)
function messaging(message, tab, callback) {
    if (background_debug_msg > 5) mpDebug.log('%c Sending message to content:', 'background-color: #0000FF; color: #FFF; padding: 3px; ', message, tab);
    else if (background_debug_msg > 4 && message.action != 'check_for_new_input_fields') mpDebug.log('%c Sending message to content:', 'background-color: #0000FF; color: #FFF; padding: 3px; ', message);

    if (isSafari) {
        //console.log( tab, tab.page );
        if (tab && tab.page) tab.page.dispatchMessage("messageFromBackground", message);
        else mooltipassEvent.onMessage({ message: message }, 1, tab);
    } else chrome.tabs.sendMessage(typeof (tab) == 'number' ? tab : tab.id, message, function (response) { });
};

/*
 * Get tabId for Safari.
 * 
 * @param tab {Object}
 * @return tabId {Integer}
 */
function getSafariTabId(tab) {
    for (var i = 0; i < safari.application.activeBrowserWindow.tabs.length; i++) {
        if (safari.application.activeBrowserWindow.tabs[i] == tab) {
            return i
        }
    }
}

function cross_notification(notificationId, options) {
    // Show notification about device status only once.
    if (notificationId.indexOf('mpNotConnected') == 0 || notificationId.indexOf('mpNotUnlocked') == 0) {
        mooltipass.backend.disableNonUnlockedNotifications = true;
    }

    // Firefox doesn't support buttons in options and refuses to show notification.
    if (isFirefox) delete options.buttons

    if (isSafari) {
        // Don't show notifications for Safari to pass OS X Human Interface Guidelines.
        // options.tag = notificationId;
        // options.body = options.message;
        // var n = new Notification( options.title, options );
        // n.onclose = mooltipassEvent.onNotifyClosed;
    } else {
        chrome.notifications.create(notificationId, options);
    }
}

// Masquerade event var into a different variable name ( while event is not reserved, many websites use it and creates problems )
var mooltipassEvent = {
    eventLoaded: true
};

// Keep event for backwards compatibility
var event = mooltipassEvent;

/**
 * Message listener - Handles the messages from the content script
 * @request {object}  The request received from content. Varies if it comes from Safari or Chrome/FF
 * @sender {object} Tab object sending the message
**/
mooltipassEvent.onMessage = function (request, sender, callback) {
    if (isSafari) { // Safari sends an EVENT
        sender = request.target;
        request = request.message;
        tab = sender;
    } else { // Chrome and FF sends Request and Sender separately
        tab = sender.tab;

        /* trade lightly below: for getStatus message ONLY we allow overwrite of the current tab object as the sender url is marked as "chrome-extension://" */
        /* worst case: another extension may ask if a given website is blacklisted */
        if (request.action == 'get_status' && (sender.url.startsWith("chrome-extension://") || sender.url.startsWith("moz-extension://"))) {
            tab = request.overwrite_tab;
        }
    }

    if (background_debug_msg > 4) mpDebug.log('%c mooltipassEvent: onMessage ' + request.action, mpDebug.css('e2eef9'), sender);

    if (request.action in mooltipassEvent.messageHandlers) {
        if (tab && request.action !== 'get_status') {
            var callback = function (data, tab) {
                messaging({ 'action': 'response-' + request.action, 'data': data }, tab);
            };
        }
        mooltipassEvent.invoke(mooltipassEvent.messageHandlers[request.action], callback, tab, request.args);
    }
    else if (tab && request.action === 'check_if_blacklisted') {
        var isBlackListed = mooltipassEvent.isBlacklisted(request.url);
        messaging({ 'action': 'response-' + request.action, 'isBlacklisted': isBlackListed }, tab); 
    }

    return true;
}

/**
 * Get interesting information about the given tab.
 * Function adapted from AdBlock-Plus.
 *
 * @param {function} handler to call after invoke
 * @param {function} callback to call after handler or null
 * @param {integer} senderTabId
 * @param {array} args
 * @param {bool} secondTime
 * @returns null (asynchronous)
 */
mooltipassEvent.invoke = function (handler, callback, senderTab, args, secondTime) {
    if (background_debug_msg > 4) mpDebug.log('%c mooltipassEvent: invoke ', mpDebug.css('e2eef9'), arguments);
    if (typeof (senderTab) == 'number') senderTab = { id: senderTab };

    if (senderTab && senderTab.id != undefined && !page.tabs[senderTab.id]) {
        page.createTabEntry(senderTab.id);
    };

    args = args || [];
    // Preppend the tab and the callback function to the arguments list
    args.unshift(senderTab);
    args.unshift(callback);
    //console.log( handler );
    handler.apply(this, args);
    return;
}


mooltipassEvent.onShowAlert = function (callback, tab, message) {
    alert(message);
}

mooltipassEvent.onLoadSettings = function (callback, tab) {
    page.settings = (typeof (localStorage.settings) == 'undefined') ? {} : JSON.parse(localStorage.settings);
    if (isFirefox || isSafari) page.settings.useMoolticute = true;
    mooltipass.backend.loadSettings();
}

mooltipassEvent.onGetSettings = function (callback, tab) {
    if (background_debug_msg > 4) mpDebug.log('%c mooltipassEvent: %c onGetSettings', 'background-color: #e2eef9', 'color: #246', tab);
    mooltipassEvent.onLoadSettings();
    var settings = page.settings;

    settings['status'] = mooltipass.device._status;
    settings['tabId'] = tab.id;
    settings['defined-credential-fields'] = settings['defined-credential-fields'] || {}
    settings['extension-base'] = isSafari
        ? safari.extension.baseURI
        : chrome.extension.getURL('/')

    callback({ data: settings }, tab);
}

mooltipassEvent.onSaveSettings = function (callback, tab, settings) {
    localStorage.settings = JSON.stringify(settings);
    mooltipassEvent.onLoadSettings();
}

mooltipassEvent.onGetStatus = function (callback, tab) {
    // This list was taken from mcCombs special combinaions.
    var twoPageAuthServices = ['login.live.com', 'accounts.autodesk.com', 'accounts.google.com', 'soundcloud.com', 'www.evernote.com', 'login.yahoo.com', 'hsbc.com', 'online.citi.com', 'upwork.com']

    if (tab) {
        browserAction.showDefault(null, tab);
        page.tabs[tab.id].errorMessage = undefined;  // XXX debug
    }

    var toReturn = {
        status: mooltipass.device.getStatus(),
        error: undefined,
        blacklisted: false,
        hideCustomCredentials: false
    };

    if (tab && tab.url) {
        toReturn.hideCustomCredentials = twoPageAuthServices.some(function (domain) { return (new URL(tab.url).hostname).match(domain) });
        var tabStatus = mooltipass.backend.extractDomainAndSubdomain(tab.url);
        toReturn.blacklisted = tabStatus.blacklisted;
    }

    if (background_debug_msg > 5) mpDebug.log('%c mooltipassEvent: %c onGetStatus', 'background-color: #e2eef9', 'color: #246', tab, toReturn);
    callback(toReturn);
}

mooltipassEvent.onPopStack = function (callback, tab) {
    browserAction.stackPop(tab.id);
    browserAction.show(null, tab);
}

mooltipassEvent.onGetTabInformation = function (callback, tab) {
    var id = tab.id || page.currentTabId;

    callback(page.tabs[id]);
}

mooltipassEvent.onGetConnectedDatabase = function (callback, tab) {
    callback({
        "count": 10,
        "identifier": 'my_mp_db_id'
    });
}

mooltipassEvent.onRemoveCredentialsFromTabInformation = function (callback, tab) {
    var id = tab.id || page.currentTabId;

    page.clearCredentials(id);
}

mooltipassEvent.onNotifyButtonClick = function (id, buttonIndex) {
    // Check the kind of notification
    if (id.indexOf('mpNotConnected') == 0 || id.indexOf('mpNotUnlocked') == 0) {
        mooltipass.backend.disableNonUnlockedNotifications = true;
    }
    else {
        // Check notification type
        if (mooltipassEvent.mpUpdate[id].type == "singledomainadd") {
            // Adding a single domain notification
            if (buttonIndex == 0) {
                // Blacklist
                //console.log('notification blacklist ',mooltipassEvent.mpUpdate[id].url);
                mooltipass.backend.blacklistUrl(mooltipassEvent.mpUpdate[id].url);
            }
            else {
            }
        }
        else if (mooltipassEvent.mpUpdate[id].type == "subdomainadd") {
            // Adding a sub domain notification
            if (buttonIndex == 0) {
                // Store credentials
                //console.log('notification update with subdomain',mooltipassEvent.mpUpdate[id].username,'on',mooltipassEvent.mpUpdate[id].url);
                mooltipass.device.updateCredentials(null, mooltipassEvent.mpUpdate[id].tab, 0, mooltipassEvent.mpUpdate[id].username, mooltipassEvent.mpUpdate[id].password, mooltipassEvent.mpUpdate[id].url);
            }
            else {
                // Store credentials
                //console.log('notification update',mooltipassEvent.mpUpdate[id].username,'on',mooltipassEvent.mpUpdate[id].url2);
                mooltipass.device.updateCredentials(null, mooltipassEvent.mpUpdate[id].tab, 0, mooltipassEvent.mpUpdate[id].username, mooltipassEvent.mpUpdate[id].password, mooltipassEvent.mpUpdate[id].url2);
            }
        }
        delete mooltipassEvent.mpUpdate[id];
    }

    // Close notification
    chrome.notifications.clear(id);
}

mooltipassEvent.onNotifyClosed = function (id) {
    delete mooltipassEvent.mpUpdate[id];
}

mooltipassEvent.notificationCount = 0;
mooltipassEvent.mpUpdate = {};

mooltipassEvent.isMooltipassUnlocked = function () {
    if (background_debug_msg > 4) mpDebug.log('%c mooltipassEvent: %c isMooltipassUnlocked', 'background-color: #e2eef9', 'color: #246', arguments);
    // prevents "Failed to send to device: Transfer failed" error when device is suddenly unplugged
    if (typeof mooltipass.device._status.state == 'undefined') {
        return false;
    }

    // If the device is not connected and not unlocked and the user disabled the notifications, return
    if (!(mooltipass.device._status.connected && mooltipass.device._status.unlocked)) {
        if (mooltipass.backend.disableNonUnlockedNotifications) {
            //console.log('Not showing a notification as they are disabled');
            return false;
        }
    }

    // Increment notification count
    mooltipassEvent.notificationCount++;
    var noteId = 'mpNotUnlocked.' + mooltipassEvent.notificationCount.toString();

    // Check that the Mooltipass app is installed and enabled
    if (!mooltipass.device.emulation_mode && !mooltipass.device.connectedToExternalApp && !mooltipass.device.connectedToApp) {
        //console.log('notify: mooltipass app not ready');
        noteId = "mpNotUnlockedStaticMooltipassAppNotReady." + mooltipassEvent.notificationCount.toString();

        // Create notification to inform user
        cross_notification(noteId,
            {
                type: 'basic',
                title: chrome.i18n.getMessage("EventJs_Notification_AppNotInstalled_Title"),
                message: chrome.i18n.getMessage("EventJs_Notification_AppNotInstalled_Message"),
                iconUrl: '/icons/warning_icon.png',
                buttons: [{ title: chrome.i18n.getMessage("EventJs_Notification_Button_HideNotifications"), iconUrl: '/icons/forbidden-icon.png' }]
            });

        return false;
    }

    // Check that our device actually is connected
    if (!mooltipass.device._status.connected) {
        //console.log('notify: device not connected');
        noteId = "mpNotUnlockedStaticMooltipassNotConnected." + mooltipassEvent.notificationCount.toString();

        // Create notification to inform user
        cross_notification(noteId, {
            type: 'basic',
            title: chrome.i18n.getMessage("EventJs_Notification_AppNotConnected_Title"),
            message: chrome.i18n.getMessage("EventJs_Notification_AppNotConnected_Message"),
            iconUrl: '/icons/warning_icon.png',
            buttons: [{ title: chrome.i18n.getMessage("EventJs_Notification_Button_HideNotifications"), iconUrl: '/icons/forbidden-icon.png' }]
        });

        return false;
    }
    else if (!mooltipass.device._status.unlocked) {
        //console.log('notify: device locked');
        noteId = "mpNotUnlockedStaticMooltipassDeviceLocked." + mooltipassEvent.notificationCount.toString();

        cross_notification(noteId,
            {
                type: 'basic',
                title: chrome.i18n.getMessage("EventJs_Notification_DeviceLocked_Title"),
                message: chrome.i18n.getMessage("EventJs_Notification_DeviceLocked_Message"),
                iconUrl: '/icons/warning_icon.png',
                buttons: [{ title: chrome.i18n.getMessage("EventJs_Notification_Button_HideNotifications"), iconUrl: '/icons/forbidden-icon.png' }]
            });

        return false;
    }
    else if (mooltipass.device._status.state == 'NoCard') {
        //console.log('notify: device without card');

        noteId = "mpNotUnlockedStaticMooltipassDeviceWithoutCard." + mooltipassEvent.notificationCount.toString();

        // Create notification to inform user
        cross_notification(noteId, {
            type: 'basic',
            title: chrome.i18n.getMessage("EventJs_Notification_NoCard_Title"),
            message: chrome.i18n.getMessage("EventJs_Notification_NoCard_Message"),
            iconUrl: '/icons/warning_icon.png',
            buttons: [{ title: chrome.i18n.getMessage("EventJs_Notification_Button_HideNotifications"), iconUrl: '/icons/forbidden-icon.png' }]
        });

        return false;
    }
    else if (mooltipass.device._status.state == 'ManageMode') {
        //console.log('notify: management mode');

        noteId = "mpNotUnlockedStaticMooltipassDeviceInManagementMode." + mooltipassEvent.notificationCount.toString();

        var isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
        var notification = {
            type: 'basic',
            title: chrome.i18n.getMessage("EventJs_Notification_ManagementMode_Title"),
            message: chrome.i18n.getMessage("EventJs_Notification_ManagementMode_Message"),
            iconUrl: '/icons/warning_icon.png'
        };

        if (!isFirefox) notification.buttons = [{ title: chrome.i18n.getMessage("EventJs_Notification_Button_HideNotifications"), iconUrl: '/icons/forbidden-icon.png' }];

        // Create notification to inform user
        cross_notification(noteId, notification);

        return false;
    }

    return true;
}

mooltipassEvent.onUpdateNotify = function (callback, tab, username, password, url, usernameExists, credentialsList) {
    if (background_debug_msg > 2) mpDebug.log('%c mooltipassEvent: %c onUpdateNotify', 'background-color: #e2eef9', 'color: #246', arguments);

    // No password? Return
    if (!password) return;

    // Parse URL
    var parsed_url = mooltipass.backend.extractDomainAndSubdomain(url);
    var valid_url = false;
    var subdomain;
    var domain;

    //console.log('onUpdateNotify', 'parsed_url', parsed_url);

    // See if our script detected a valid domain & subdomain
    if (parsed_url.valid == true) {
        valid_url = true;
        domain = parsed_url.domain;
        subdomain = parsed_url.subdomain;
    }

    // Check if URL is valid
    if (valid_url == true) {
        // Get URI
        var toBeProcessedUrl = subdomain ? subdomain + '.' + domain : domain;

        // Check if blacklisted
        if (mooltipass.backend.isBlacklisted(domain) || mooltipass.backend.isBlacklisted(toBeProcessedUrl)) {
            //console.log('notify: ignoring blacklisted url',url);
            return;
        }

        // Check that the Mooltipass is unlocked
        var mp_unlocked = mooltipassEvent.isMooltipassUnlocked();

        // Increment notification count
        mooltipassEvent.notificationCount++;

        if (mp_unlocked && password.length > 31) {
            var noteId = 'mpPasswordTooLong.' + mooltipassEvent.notificationCount.toString();

            cross_notification(noteId, {
                type: 'basic',
                title: chrome.i18n.getMessage("EventJs_Notification_PassTooLong_Title"),
                message: chrome.i18n.getMessage("EventJs_Notification_PassTooLong_Message"),
                iconUrl: '/icons/warning_icon.png'
            });
            return;
        }

        if (subdomain == null) {
            // Single domain
            // Here we should send a request to the mooltipass to know if the username exists!
            if (true) {
                // Unknown user
                var noteId = 'mpUpdate.' + mooltipassEvent.notificationCount.toString();

                // Store our event
                mooltipassEvent.mpUpdate[noteId] = { tab: tab, username: username, password: password, url: domain, url2: domain, type: "singledomainadd" };

                // Send request by default
                mooltipass.device.updateCredentials(null, tab, 0, username, password, domain);

                // Create notification to blacklist
                if (mooltipass.device._status.unlocked) {
                    cross_notification(noteId,
                        {
                            type: 'basic',
                            title: chrome.i18n.getMessage("EventJs_Notification_DetectedCredentials_Title"),
                            message: chrome.i18n.getMessage("EventJs_Notification_DetectedCredentials_Message"),
                            iconUrl: '/icons/mooltipass-128.png',
                            buttons: [{ title: chrome.i18n.getMessage("EventJs_Notification_Button_BlackList"), iconUrl: '/icons/forbidden-icon.png' }]
                        },
                        function (id) {
                            //console.log('notification created for',id);
                        });
                }
            }
            else { }
        }
        else {
            // Subdomain exists
            // Here we should send a request to the mooltipass to know if the username exists!

            // first let's check to make sure the device is connected
            if (mooltipass.device._status.state == 'NotConnected') {
                //console.log('mooltipass not connected - do not ask which domain to store');
            }
            else if (mooltipass.device.emulation_mode) {
                var notification = {
                    type: 'basic',
                    title: chrome.i18n.getMessage("EventJs_Notification_SubDomainDetected_Title"),
                    message: chrome.i18n.getMessage("EventJs_Notification_SubDomainDetected_Message_1"),
                    iconUrl: '/icons/question.png',
                };

                mooltipass.device.updateCredentials(null, tab, 0, username, password, domain);
                cross_notification(noteId, notification);
            }
            else {

                // Unknown user
                var noteId = 'mpUpdate.' + mooltipassEvent.notificationCount.toString();

                // Store our event
                mooltipassEvent.mpUpdate[noteId] = { tab: tab, username: username, password: password, url: domain, url2: subdomain + "." + domain, type: "subdomainadd" };

                var isFirefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
                var notification = {
                    type: 'basic',
                    title: chrome.i18n.getMessage("EventJs_Notification_SubDomainDetected_Title"),
                    message: chrome.i18n.getMessage("EventJs_Notification_SubDomainDetected_Message_2"),
                    iconUrl: '/icons/question.png',
                };

                // Firefox doesn't support buttons on notifications
                if (!isFirefox && !isSafari) {
                    notification.buttons = [{ title: chrome.i18n.getMessage("EventJs_Notification_Button_StoreDomain") + domain }, { title: chrome.i18n.getMessage("EventJs_Notification_Button_StoreDomain") + subdomain + '.' + domain }];
                    notification.requireInteraction = true;
                } else {
                    // Firefox: Use domain (we should check against subdomain and later domain if missing tho...)
                    notification.message = chrome.i18n.getMessage("EventJs_Notification_SubDomainDetected_Message_1");
                    mooltipass.device.updateCredentials(null, tab, 0, username, password, subdomain + '.' + domain);
                }

                cross_notification(noteId, notification);
            }
        }
    }
}

mooltipassEvent.onUpdate = function (callback, tab, username, password, url, usernameExists, credentialsList) {
    mooltipass.device.updateCredentials(callback, tab, 0, username, password, url);
}

mooltipassEvent.onLoginPopup = function (callback, tab, logins) {
    var stackData = {
        level: 1,
        iconType: "questionmark",
        popup: "popup_login.html"
    }
    browserAction.stackUnshift(stackData, tab.id);

    page.tabs[tab.id].loginList = logins;

    browserAction.show(null, tab);
}

mooltipassEvent.onHTTPAuthPopup = function (callback, tab, data) {
    var stackData = {
        level: 1,
        iconType: "questionmark",
        popup: "popup_httpauth.html"
    }
    browserAction.stackUnshift(stackData, tab.id);

    page.tabs[tab.id].loginList = data;

    browserAction.show(null, tab);
}

mooltipassEvent.onMultipleFieldsPopup = function (callback, tab) {
    var stackData = {
        level: 1,
        iconType: "normal",
        popup: "popup_multiple-fields.html"
    }
    browserAction.stackUnshift(stackData, tab.id);

    browserAction.show(null, tab);
}

/*
 * Open either Chrome App or Moolticute Interface
 *
 */
mooltipassEvent.showApp = function () {

    if (mooltipass.device.isConnectedToExternalApp()) {
        moolticute.sendRequest({ "msg": "show_app" });
    } else {
        var global = chrome.extension.getBackgroundPage();
        chrome.runtime.sendMessage(global.mooltipass.device._app.id, { 'show': true });
    }
}

/*
 * Notify httpAuth when custom HTTP Auth form is submitted.
 */
mooltipassEvent.onHttpAuthSubmit = function (callback, tab, credentials) {
    httpAuth.onSubmit(credentials)
}

/*
 * Recreate action based on arguments. Used for tab and iframe communication.
 */
mooltipassEvent.createAction = function (callback, tab, data) {
    messaging({
        action: data.action,
        args: data.args
    }, tab)
}

/*
 * Check if a given URL is BlackListed
 */
mooltipassEvent.isBlacklisted = function (url) {
    // Parse URL
    var parsed_url = mooltipass.backend.extractDomainAndSubdomain(url);
    var valid_url = false;
    var isBlackListed = false;
    var subdomain;
    var domain;

    // See if our script detected a valid domain & subdomain
    if (parsed_url.valid == true) {
        valid_url = true;
        domain = parsed_url.domain;
        subdomain = parsed_url.subdomain;
    }

    // Check if URL is valid
    if (valid_url == true) {
        // Get URI
        var toBeProcessedUrl = subdomain ? subdomain + '.' + domain : domain;

        // Check if blacklisted
        if (mooltipass.backend.isBlacklisted(domain) || mooltipass.backend.isBlacklisted(toBeProcessedUrl)) isBlackListed = true;
    }

    return isBlackListed;
}

// all methods named in this object have to be declared BEFORE this!
mooltipassEvent.messageHandlers = {
    'update': mooltipassEvent.onUpdate,
    'add_credentials': mooltipass.device.addCredentials,
    'blacklist_url': mooltipass.backend.handlerBlacklistUrl,
    'unblacklist_url': mooltipass.backend.handlerUnBlacklistUrl,
    'blacklistUrl': mooltipass.backend.blacklistUrl,
    'alert': mooltipassEvent.onShowAlert,
    'get_connected_database': mooltipassEvent.onGetConnectedDatabase,
    'get_settings': mooltipassEvent.onGetSettings,
    'get_status': mooltipassEvent.onGetStatus,
    'get_tab_information': mooltipassEvent.onGetTabInformation,
    'load_settings': mooltipassEvent.onLoadSettings,
    'pop_stack': mooltipassEvent.onPopStack,
    'popup_login': mooltipassEvent.onLoginPopup,
    'popup_multiple-fields': mooltipassEvent.onMultipleFieldsPopup,
    'remove_credentials_from_tab_information': mooltipassEvent.onRemoveCredentialsFromTabInformation,
    'retrieve_credentials': mooltipass.device.retrieveCredentials,
    'show_default_browseraction': browserAction.showDefault,
    'update_credentials': mooltipass.device.updateCredentials,
    'save_settings': mooltipassEvent.onSaveSettings,
    'update_notify': mooltipassEvent.onUpdateNotify,
    'stack_add': browserAction.stackAdd,
    'http_auth_submit': mooltipassEvent.onHttpAuthSubmit,
    'generate_password': mooltipass.device.generatePassword,
    'set_current_tab': page.setCurrentTab,
    'cache_login': page.cacheLogin,
    'cache_retrieve': page.cacheRetrieve,
    'content_script_loaded': page.setAllLoaded,
    'show_app': mooltipassEvent.showApp,
    'create_action': mooltipassEvent.createAction
};

if (!isSafari) {
    chrome.notifications.onButtonClicked.addListener(mooltipassEvent.onNotifyButtonClick);
    chrome.notifications.onClosed.addListener(mooltipassEvent.onNotifyClosed);
}
