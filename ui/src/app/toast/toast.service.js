/* eslint-disable import/no-unresolved, import/default */

import infoToast from './info-toast.tpl.html';
import successToast from './success-toast.tpl.html';
import errorToast from './error-toast.tpl.html';

/* eslint-enable import/no-unresolved, import/default */

/*@ngInject*/
export default function Toast($mdToast, $document) {

    var showing = false;

    var service = {
        showInfo: showInfo,
        showSuccess: showSuccess,
        showError: showError,
        hide: hide
    }

    return service;

    function showInfo(infoMessage, delay, toastParent, position) {
        showMessage(infoToast, infoMessage, delay, toastParent, position);
    }

    function showSuccess(successMessage, delay, toastParent, position) {
        showMessage(successToast, successMessage, delay, toastParent, position);
    }

    function showMessage(templateUrl, message, delay, toastParent, position) {
        if (!toastParent) {
            toastParent = angular.element($document[0].getElementById('toast-parent'));
        }
        if (!position) {
            position = 'top left';
        }
        $mdToast.show({
            hideDelay: delay || 0,
            position: position,
            controller: 'ToastController',
            controllerAs: 'vm',
            templateUrl: templateUrl,
            locals: {message: message},
            parent: toastParent
        });
    }

    function showError(errorMessage, toastParent, position) {
        if (!showing) {
            if (!toastParent) {
                toastParent = angular.element($document[0].getElementById('toast-parent'));
            }
            if (!position) {
                position = 'top left';
            }
            showing = true;
            $mdToast.show({
                hideDelay: 0,
                position: position,
                controller: 'ToastController',
                controllerAs: 'vm',
                templateUrl: errorToast,
                locals: {message: errorMessage},
                parent: toastParent
            }).then(function hide() {
                showing = false;
            }, function cancel() {
                showing = false;
            });
        }
    }

    function hide() {
        if (showing) {
            $mdToast.hide();
        }
    }

}