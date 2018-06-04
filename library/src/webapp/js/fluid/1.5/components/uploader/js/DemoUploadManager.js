var fluid_1_5=fluid_1_5||{};!function($,fluid){"use strict";fluid.registerNamespace("fluid.uploader.demo"),fluid.uploader.demo.uploadNextFile=function(that){that.demoState.currentFile=that.queue.files[that.demoState.fileIdx],that.demoState.chunksForCurrentFile=Math.ceil(that.demoState.currentFile/that.demoState.chunkSize),that.demoState.bytesUploaded=0,that.queue.isUploading=!0,that.events.onFileStart.fire(that.demoState.currentFile),fluid.uploader.demo.simulateUpload(that)},fluid.uploader.demo.updateProgress=function(file,events,demoState,isUploading){if(isUploading){var chunk=Math.min(demoState.chunkSize,file.size);demoState.bytesUploaded=Math.min(demoState.bytesUploaded+chunk,file.size),events.onFileProgress.fire(file,demoState.bytesUploaded,file.size)}},fluid.uploader.demo.finishAndContinueOrCleanup=function(that,file){that.queue.finishFile(file),that.events.afterFileComplete.fire(file),that.queue.shouldUploadNextFile()?fluid.uploader.demo.uploadNextFile(that):(that.events.afterUploadComplete.fire(that.queue.currentBatch.files),file.status!==fluid.uploader.fileStatusConstants.CANCELLED&&that.queue.clearCurrentBatch())},fluid.uploader.demo.finishUploading=function(that){if(that.queue.isUploading){var file=that.demoState.currentFile;that.events.onFileSuccess.fire(file),that.demoState.fileIdx++,fluid.uploader.demo.finishAndContinueOrCleanup(that,file)}},fluid.uploader.demo.simulateUpload=function(that){if(that.queue.isUploading){var file=that.demoState.currentFile;that.demoState.bytesUploaded<file.size?fluid.invokeAfterRandomDelay(function(){fluid.uploader.demo.updateProgress(file,that.events,that.demoState,that.queue.isUploading),fluid.uploader.demo.simulateUpload(that)}):fluid.uploader.demo.finishUploading(that)}},fluid.uploader.demo.stop=function(that){var file=that.demoState.currentFile;file.filestatus=fluid.uploader.fileStatusConstants.CANCELLED,that.queue.shouldStop=!0,that.events.onFileError.fire(file,fluid.uploader.errorConstants.UPLOAD_STOPPED,"The demo upload was paused by the user."),fluid.uploader.demo.finishAndContinueOrCleanup(that,file),that.events.onUploadStop.fire()},fluid.demands("fluid.uploader.uploadNextFile","fluid.uploader.demo.remote",{funcName:"fluid.uploader.demo.uploadNextFile",args:"{that}"}),fluid.demands("fluid.uploader.stop","fluid.uploader.demo.remote",{funcName:"fluid.uploader.demo.stop",args:"{that}"}),fluid.invokeAfterRandomDelay=function(fn){var delay=Math.floor(200*Math.random()+100);setTimeout(fn,delay)},fluid.defaults("fluid.uploader.demo.remote",{gradeNames:["fluid.uploader.remote","autoInit"],members:{demoState:{fileIdx:0,chunkSize:2e5}}}),fluid.demands("fluid.uploader.remote",["fluid.uploader.multiFileUploader","fluid.uploader.demo"],{funcName:"fluid.uploader.demo.remote"})}(jQuery,fluid_1_5);