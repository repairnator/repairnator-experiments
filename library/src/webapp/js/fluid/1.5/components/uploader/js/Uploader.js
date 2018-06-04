var fluid_1_5=fluid_1_5||{};!function($,fluid){"use strict";fluid.registerNamespace("fluid.uploader"),fluid.uploader.fileOrFiles=function(that,numFiles){return 1===numFiles?that.options.strings.progress.singleFile:that.options.strings.progress.pluralFiles},fluid.uploader.enableElement=function(that,elm){elm.prop("disabled",!1),elm.removeClass(that.options.styles.dim)},fluid.uploader.disableElement=function(that,elm){elm.prop("disabled",!0),elm.addClass(that.options.styles.dim)},fluid.uploader.showElement=function(that,elm){elm.removeClass(that.options.styles.hidden)},fluid.uploader.hideElement=function(that,elm){elm.addClass(that.options.styles.hidden)},fluid.uploader.maxFilesUploaded=function(that){var fileUploadLimit=that.queue.getUploadedFiles().length+that.queue.getReadyFiles().length+that.queue.getErroredFiles().length;return fileUploadLimit===that.options.queueSettings.fileUploadLimit},fluid.uploader.setTotalProgressStyle=function(that,didError){didError=didError||!1;var indicator=that.totalProgress.indicator;indicator.toggleClass(that.options.styles.totalProgress,!didError),indicator.toggleClass(that.options.styles.totalProgressError,didError)},fluid.uploader.setStateEmpty=function(that){fluid.uploader.disableElement(that,that.locate("uploadButton")),0===that.queue.files.length&&(that.locate("browseButtonText").text(that.options.strings.buttons.browse),that.locate("browseButton").removeClass(that.options.styles.browseButton),fluid.uploader.showElement(that,that.locate("instructions")))},fluid.uploader.enableBrowseButton=function(that){fluid.uploader.maxFilesUploaded(that)||(fluid.uploader.enableElement(that,that.locate("browseButton")),that.strategy.local.enableBrowseButton())},fluid.uploader.setStateDone=function(that){fluid.uploader.disableElement(that,that.locate("uploadButton")),fluid.uploader.hideElement(that,that.locate("pauseButton")),fluid.uploader.showElement(that,that.locate("uploadButton")),fluid.uploader.enableBrowseButton(that)},fluid.uploader.setStateLoaded=function(that){that.locate("browseButtonText").text(that.options.strings.buttons.addMore),that.locate("browseButton").addClass(that.options.styles.browseButton),fluid.uploader.hideElement(that,that.locate("pauseButton")),fluid.uploader.showElement(that,that.locate("uploadButton")),fluid.uploader.enableElement(that,that.locate("uploadButton")),fluid.uploader.hideElement(that,that.locate("instructions")),that.totalProgress.hide(),fluid.uploader.enableBrowseButton(that)},fluid.uploader.setStateUploading=function(that){that.totalProgress.hide(!1,!1),fluid.uploader.setTotalProgressStyle(that),fluid.uploader.hideElement(that,that.locate("uploadButton")),fluid.uploader.disableElement(that,that.locate("browseButton")),that.strategy.local.disableBrowseButton(),fluid.uploader.enableElement(that,that.locate("pauseButton")),fluid.uploader.showElement(that,that.locate("pauseButton"))},fluid.uploader.setStateFull=function(that){that.locate("browseButtonText").text(that.options.strings.buttons.addMore),that.locate("browseButton").addClass(that.options.styles.browseButton),fluid.uploader.hideElement(that,that.locate("pauseButton")),fluid.uploader.showElement(that,that.locate("uploadButton")),fluid.uploader.enableElement(that,that.locate("uploadButton")),fluid.uploader.disableElement(that,that.locate("browseButton")),that.strategy.local.disableBrowseButton(),fluid.uploader.hideElement(that,that.locate("instructions")),that.totalProgress.hide()},fluid.uploader.renderUploadTotalMessage=function(that){var numReadyFiles=that.queue.getReadyFiles().length,bytesReadyFiles=that.queue.sizeOfReadyFiles(),fileLabelStr=fluid.uploader.fileOrFiles(that,numReadyFiles),totalCount=that.queue.files.length,noFilesMsg=that.options.strings.progress.noFiles,totalStateStr=fluid.stringTemplate(that.options.strings.progress.toUploadLabel,{fileCount:numReadyFiles,fileLabel:fileLabelStr,totalBytes:fluid.uploader.formatFileSize(bytesReadyFiles),uploadedCount:that.queue.getUploadedFiles().length,uploadedSize:fluid.uploader.formatFileSize(that.queue.sizeOfUploadedFiles()),totalCount:totalCount,totalSize:fluid.uploader.formatFileSize(that.queue.totalBytes())});!totalCount&&noFilesMsg&&(totalStateStr=noFilesMsg),that.locate("totalFileStatusText").html(totalStateStr)},fluid.uploader.renderFileUploadLimit=function(that){if(that.options.queueSettings.fileUploadLimit>0){var fileUploadLimitText=fluid.stringTemplate(that.options.strings.progress.fileUploadLimitLabel,{fileUploadLimit:that.options.queueSettings.fileUploadLimit,fileLabel:fluid.uploader.fileOrFiles(that,that.options.queueSettings.fileUploadLimit)});that.locate("fileUploadLimitText").html(fileUploadLimitText)}},fluid.uploader.formatFileSize=function(bytes){if("number"==typeof bytes){if(0===bytes)return"0.0 KB";if(bytes>0)return 1048576>bytes?(Math.ceil(bytes/1024*10)/10).toFixed(1)+" KB":(Math.ceil(bytes/1048576*10)/10).toFixed(1)+" MB"}return""},fluid.uploader.derivePercent=function(num,total){return Math.round(100*num/total)},fluid.uploader.updateTotalProgress=function(that){var batch=that.queue.currentBatch,totalPercent=fluid.uploader.derivePercent(batch.totalBytesUploaded,batch.totalBytes),numFilesInBatch=batch.files.length,fileLabelStr=fluid.uploader.fileOrFiles(that,numFilesInBatch),uploadingSize=batch.totalBytesUploaded+that.queue.sizeOfUploadedFiles(),totalProgressStr=fluid.stringTemplate(that.options.strings.progress.totalProgressLabel,{curFileN:batch.fileIdx,totalFilesN:numFilesInBatch,fileLabel:fileLabelStr,currBytes:fluid.uploader.formatFileSize(batch.totalBytesUploaded),totalBytes:fluid.uploader.formatFileSize(batch.totalBytes),uploadedCount:that.queue.getUploadedFiles().length,uploadedSize:fluid.uploader.formatFileSize(uploadingSize),totalCount:that.queue.files.length,totalSize:fluid.uploader.formatFileSize(that.queue.totalBytes())});that.totalProgress.update(totalPercent,totalProgressStr)},fluid.uploader.updateTotalAtCompletion=function(that){var numErroredFiles=that.queue.getErroredFiles().length,numTotalFiles=that.queue.files.length,fileLabelStr=fluid.uploader.fileOrFiles(that,numTotalFiles),errorStr="";if(numErroredFiles>0){var errorLabelString=1===numErroredFiles?that.options.strings.progress.singleError:that.options.strings.progress.pluralErrors;fluid.uploader.setTotalProgressStyle(that,!0),errorStr=fluid.stringTemplate(that.options.strings.progress.numberOfErrors,{errorsN:numErroredFiles,errorLabel:errorLabelString})}var totalProgressStr=fluid.stringTemplate(that.options.strings.progress.completedLabel,{curFileN:that.queue.getUploadedFiles().length,totalFilesN:numTotalFiles,errorString:errorStr,fileLabel:fileLabelStr,totalCurrBytes:fluid.uploader.formatFileSize(that.queue.sizeOfUploadedFiles()),uploadedCount:that.queue.getUploadedFiles().length,uploadedSize:fluid.uploader.formatFileSize(that.queue.sizeOfUploadedFiles()),totalCount:that.queue.files.length,totalSize:fluid.uploader.formatFileSize(that.queue.totalBytes())});that.totalProgress.update(100,totalProgressStr)},fluid.uploader.updateStateAfterFileDialog=function(that){var queueLength=that.queue.getReadyFiles().length;queueLength>0&&(fluid.uploader[queueLength===that.options.queueSettings.fileUploadLimit?"setStateFull":"setStateLoaded"](that),fluid.uploader.renderUploadTotalMessage(that),that.locate(that.options.focusWithEvent.afterFileDialog).focus())},fluid.uploader.updateStateAfterFileRemoval=function(that){fluid.uploader[0===that.queue.getReadyFiles().length?"setStateEmpty":"setStateLoaded"](that),fluid.uploader.renderUploadTotalMessage(that)},fluid.uploader.updateStateAfterCompletion=function(that){fluid.uploader[0===that.queue.getReadyFiles().length?"setStateDone":"setStateLoaded"](that),fluid.uploader.updateTotalAtCompletion(that)},fluid.uploader.uploadNextOrFinish=function(that){that.queue.shouldUploadNextFile()?that.strategy.remote.uploadNextFile():(that.events.afterUploadComplete.fire(that.queue.currentBatch.files),that.queue.clearCurrentBatch())},fluid.uploader.onFileStart=function(file,queue){file.filestatus=fluid.uploader.fileStatusConstants.IN_PROGRESS,queue.startFile()},fluid.uploader.onFileProgress=function(that,currentBytes){that.queue.updateBatchStatus(currentBytes),fluid.uploader.updateTotalProgress(that)},fluid.uploader.onFileComplete=function(file,that){that.queue.finishFile(file),that.events.afterFileComplete.fire(file),fluid.uploader.uploadNextOrFinish(that)},fluid.uploader.onFileSuccess=function(file,that){file.filestatus=fluid.uploader.fileStatusConstants.COMPLETE,0===that.queue.currentBatch.bytesUploadedForFile&&(that.queue.currentBatch.totalBytesUploaded+=file.size),fluid.uploader.updateTotalProgress(that)},fluid.uploader.onFileError=function(file,error,that){error===fluid.uploader.errorConstants.UPLOAD_STOPPED?file.filestatus=fluid.uploader.fileStatusConstants.CANCELLED:(file.filestatus=fluid.uploader.fileStatusConstants.ERROR,that.queue.isUploading&&(that.queue.currentBatch.totalBytesUploaded+=file.size,that.queue.currentBatch.numFilesErrored++,fluid.uploader.uploadNextOrFinish(that)))},fluid.uploader.afterUploadComplete=function(that){that.queue.isUploading=!1,fluid.uploader.updateStateAfterCompletion(that)},fluid.uploaderImpl=function(){fluid.fail('Error creating uploader component - please make sure that a progressiveCheckerForComponent for "fluid.uploader" is registered either in the static environment or else is visible in the current component tree')},fluid.enhance.check({"fluid.browser.supportsBinaryXHR":"fluid.enhance.supportsBinaryXHR","fluid.browser.supportsFormData":"fluid.enhance.supportsFormData"}),fluid.defaults("fluid.uploader",{gradeNames:["fluid.viewComponent","autoInit"],components:{uploaderContext:{type:"fluid.progressiveCheckerForComponent",options:{componentName:"fluid.uploader"}},uploaderImpl:{type:"fluid.uploaderImpl",container:"{uploader}.container"}},returnedPath:"uploaderImpl",distributeOptions:{source:"{that}.options",removeSource:!0,exclusions:["components.uploaderContext","components.uploaderImpl"],target:"{that > uploaderImpl}.options"},progressiveCheckerOptions:{checks:[{feature:"{fluid.browser.supportsBinaryXHR}",contextName:"fluid.uploader.html5"}],defaultContextName:"fluid.uploader.singleFile"}}),fluid.uploader.demoTypeTag=function(demo){return demo?"fluid.uploader.demo":"fluid.uploader.live"},fluid.uploader.browse=function(queue,localStrategy){queue.isUploading||localStrategy.browse()},fluid.uploader.removeFile=function(queue,localStrategy,afterFileRemoved,file){queue.removeFile(file),localStrategy.removeFile(file),afterFileRemoved.fire(file)},fluid.uploader.start=function(queue,remoteStrategy,onUploadStart){queue.start(),onUploadStart.fire(queue.currentBatch.files),remoteStrategy.uploadNextFile()},fluid.uploader.stop=function(remoteStrategy,onUploadStop){onUploadStop.fire(),remoteStrategy.stop()},fluid.uploader.defaultQueueSettings={uploadURL:"",postParams:{},fileSizeLimit:"20480",fileTypes:null,fileTypesDescription:null,fileUploadLimit:0,fileQueueLimit:0},fluid.uploader.bindFocus=function(focusWithEvent,noAutoFocus,events,dom){fluid.each(focusWithEvent,function(element,event){noAutoFocus[event]||events[event].addListener(function(){dom.locate(element).focus()})})},fluid.defaults("fluid.uploader.multiFileUploader",{gradeNames:["fluid.viewComponent","autoInit"],nickName:"uploader",members:{totalFileStatusTextId:{expander:{funcName:"fluid.allocateSimpleId",args:"{that}.dom.totalFileStatusText"}}},invokers:{browse:{funcName:"fluid.uploader.browse",args:["{that}.queue","{that}.strategy.local"]},removeFile:{funcName:"fluid.uploader.removeFile",args:["{that}.queue","{that}.strategy.local","{that}.events.afterFileRemoved","{arguments}.0"]},start:{funcName:"fluid.uploader.start",args:["{that}.queue","{that}.strategy.remote","{that}.events.onUploadStart"]},stop:{funcName:"fluid.uploader.stop",args:["{that}.strategy.remote","{that}.events.onUploadStop"]}},components:{demoTag:{type:"fluid.typeFount",options:{targetTypeName:{expander:{funcName:"fluid.uploader.demoTypeTag",args:"{uploader}.options.demo"}}}},queue:{type:"fluid.uploader.fileQueue"},strategy:{type:"fluid.uploader.strategy"},errorPanel:{type:"fluid.uploader.errorPanel"},fileQueueView:{type:"fluid.uploader.fileQueueView",options:{model:"{uploader}.queue.files",uploaderContainer:"{uploader}.container",strings:{buttons:{remove:"{uploader}.options.strings.buttons.remove"}}}},totalProgress:{type:"fluid.progress",container:"{uploader}.container",options:{selectors:{progressBar:".flc-uploader-queue-footer",displayElement:".flc-uploader-total-progress",label:".flc-uploader-total-progress-text",indicator:".flc-uploader-total-progress",ariaElement:".flc-uploader-total-progress"}}}},queueSettings:fluid.uploader.defaultQueueSettings,demo:!1,selectors:{fileQueue:".flc-uploader-queue",browseButton:".flc-uploader-button-browse",browseButtonText:".flc-uploader-button-browse-text",uploadButton:".flc-uploader-button-upload",pauseButton:".flc-uploader-button-pause",totalFileStatusText:".flc-uploader-total-progress-text",fileUploadLimitText:".flc-uploader-upload-limit-text",instructions:".flc-uploader-browse-instructions",errorsPanel:".flc-uploader-errorsPanel"},noAutoFocus:{afterFileDialog:!0},focusWithEvent:{afterFileDialog:"uploadButton",onUploadStart:"pauseButton",onUploadStop:"uploadButton"},styles:{disabled:"fl-uploader-disabled",hidden:"fl-uploader-hidden",dim:"fl-uploader-dim",totalProgress:"fl-uploader-total-progress-okay",totalProgressError:"fl-uploader-total-progress-errored",browseButton:"fl-uploader-browseMore"},events:{afterReady:null,onFileDialog:null,onFilesSelected:null,onFileQueued:null,afterFileQueued:null,onFileRemoved:null,afterFileRemoved:null,afterFileDialog:null,onUploadStart:null,onUploadStop:null,onFileStart:null,onFileProgress:null,onFileError:null,onQueueError:null,onFileSuccess:null,onFileComplete:null,afterFileComplete:null,afterUploadComplete:null},listeners:{onCreate:[{listener:"fluid.uploader.bindFocus",args:["{that}.options.focusWithEvent","{that}.options.noAutoFocus","{that}.events","{that}.dom"]},{"this":"{that}.dom.uploadButton",method:"click",args:"{that}.start"},{"this":"{that}.dom.pauseButton",method:"click",args:"{that}.stop"},{"this":"{that}.dom.totalFileStatusText",method:"attr",args:[{role:"log","aria-live":"assertive","aria-relevant":"text","aria-atomic":"true"}]},{"this":"{that}.dom.fileQueue",method:"attr",args:[{"aria-controls":"{that}.totalFileStatusTextId","aria-labelledby":"{that}.totalFileStatusTextId"}]}],"afterFileDialog.uploader":{listener:"fluid.uploader.updateStateAfterFileDialog",args:"{that}"},"afterFileQueued.uploader":{listener:"{that}.queue.addFile",args:"{arguments}.0"},"onFileRemoved.uploader":{listener:"{that}.removeFile",args:"{arguments}.0"},"afterFileRemoved.uploader":{listener:"fluid.uploader.updateStateAfterFileRemoval",args:"{that}"},"onUploadStart.uploader":{listener:"fluid.uploader.setStateUploading",args:"{that}"},"onFileStart.uploader":{listener:"fluid.uploader.onFileStart",args:["{arguments}.0","{that}.queue"]},"onFileProgress.uploader":{listener:"fluid.uploader.onFileProgress",args:["{that}","{arguments}.1"]},"onFileComplete.uploader":{listener:"fluid.uploader.onFileComplete",args:["{arguments}.0","{that}"]},"onFileSuccess.uploader":{listener:"fluid.uploader.onFileSuccess",args:["{arguments}.0","{that}"]},"onFileError.uploader":{listener:"fluid.uploader.onFileError",args:["{arguments}.0","{arguments}.1","{that}"]},"afterUploadComplete.uploader":{listener:"fluid.uploader.afterUploadComplete",args:"{that}"}},strings:{progress:{fileUploadLimitLabel:"%fileUploadLimit %fileLabel maximum",noFiles:"0 files",toUploadLabel:"%uploadedCount out of %totalCount files uploaded (%uploadedSize of %totalSize)",totalProgressLabel:"%uploadedCount out of %totalCount files uploaded (%uploadedSize of %totalSize)",completedLabel:"%uploadedCount out of %totalCount files uploaded (%uploadedSize of %totalSize)%errorString",numberOfErrors:", %errorsN %errorLabel",singleFile:"file",pluralFiles:"files",singleError:"error",pluralErrors:"errors"},buttons:{browse:"Browse Files",addMore:"Add More",stopUpload:"Stop Upload",cancelRemaning:"Cancel remaining Uploads",resumeUpload:"Resume Upload",remove:"Remove"}}}),fluid.uploader.multiFileUploader.finalInit=function(that){fluid.uploader.disableElement(that,that.locate("uploadButton")),fluid.uploader.renderFileUploadLimit(that);var noFilesMsg=that.options.strings.progress.noFiles;noFilesMsg&&that.locate("totalFileStatusText").text(noFilesMsg),that.container.attr("role","application")},fluid.defaults("fluid.uploader.strategy",{gradeNames:["fluid.littleComponent"],components:{local:{type:"fluid.uploader.local"},remote:{type:"fluid.uploader.remote"}}}),fluid.defaults("fluid.uploader.local",{gradeNames:["fluid.eventedComponent"],queueSettings:"{uploader}.options.queueSettings",members:{queue:"{uploader}.queue"},events:{onFileDialog:"{uploader}.events.onFileDialog",onFilesSelected:"{uploader}.events.onFilesSelected",afterFileDialog:"{uploader}.events.afterFileDialog",afterFileQueued:"{uploader}.events.afterFileQueued",onQueueError:"{uploader}.events.onQueueError"},invokers:{enableBrowseButton:"fluid.uploader.local.enableBrowseButton",disableBrowseButton:"fluid.uploader.local.disableBrowseButton"}}),fluid.defaults("fluid.uploader.remote",{gradeNames:["fluid.eventedComponent"],members:{queue:"{uploader}.queue",queueSettings:"{uploader}.options.queueSettings"},events:{onFileStart:"{uploader}.events.onFileStart",onFileProgress:"{uploader}.events.onFileProgress",onFileSuccess:"{uploader}.events.onFileSuccess",onFileError:"{uploader}.events.onFileError",onFileComplete:"{uploader}.events.onFileComplete",onUploadStop:"{uploader}.events.onUploadStop",afterFileComplete:"{uploader}.events.afterFileComplete",afterUploadComplete:"{uploader}.events.afterUploadComplete"},invokers:{uploadNextFile:"fluid.uploader.uploadNextFile",stop:"fluid.uploader.stop"}}),fluid.demands("fluid.uploader.fileQueueView","fluid.uploader.multiFileUploader",{container:"{uploader}.dom.fileQueue",options:{events:{onFileRemoved:"{uploader}.events.onFileRemoved"}}}),fluid.demands("fluid.uploader.fileQueueView.eventBinder",["fluid.uploader.multiFileUploader","fluid.uploader.fileQueueView"],{options:{listeners:{"{uploader}.events.afterFileQueued":"{fileQueueView}.addFile","{uploader}.events.onUploadStart":"{fileQueueView}.prepareForUpload","{uploader}.events.onFileStart":"{fileQueueView}.showFileProgress","{uploader}.events.onFileProgress":"{fileQueueView}.updateFileProgress","{uploader}.events.onFileSuccess":"{fileQueueView}.markFileComplete","{uploader}.events.onFileError":"{fileQueueView}.showErrorForFile","{uploader}.events.afterFileComplete":"{fileQueueView}.hideFileProgress","{uploader}.events.afterUploadComplete":"{fileQueueView}.refreshAfterUpload"}}}),fluid.uploader.queueErrorConstants={QUEUE_LIMIT_EXCEEDED:"queue limit exceeded",FILE_EXCEEDS_SIZE_LIMIT:"file exceeds size limit",ZERO_BYTE_FILE:"zero byte file",INVALID_FILETYPE:"invalid filetype"},fluid.uploader.errorConstants={HTTP_ERROR:"HTTP error",MISSING_UPLOAD_URL:"Missing upload URL",IO_ERROR:"I/O error",SECURITY_ERROR:"Security error",UPLOAD_LIMIT_EXCEEDED:"Upload limit exceeded",UPLOAD_FAILED:"Uploader failed",SPECIFIED_FILE_ID_NOT_FOUND:"Specified file ID not found",FILE_VALIDATION_FAILED:"File validation failed",FILE_CANCELLED:"File cancelled",UPLOAD_STOPPED:"Upload stopped"},fluid.uploader.fileStatusConstants={QUEUED:"queued",IN_PROGRESS:"in progress",ERROR:"error",COMPLETE:"complete",CANCELLED:"cancelled"};var toggleVisibility=function(toShow,toHide){window.opera?(toShow.show().removeClass("hideUploaderForOpera"),toHide.show().addClass("hideUploaderForOpera")):(toShow.show(),toHide.hide())};fluid.defaults("fluid.uploader.singleFileUploader",{gradeNames:["fluid.viewComponent","autoInit"],selectors:{basicUpload:".fl-progEnhance-basic"}}),fluid.uploader.singleFileUploader.finalInit=function(that){toggleVisibility($(that.options.selectors.basicUpload),that.container)},fluid.demands("fluid.uploaderImpl","fluid.uploader.singleFile",{funcName:"fluid.uploader.singleFileUploader"})}(jQuery,fluid_1_5);