package org.opentosca.toscana.core.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.opentosca.toscana.core.plugin.lifecycle.ExecutionPhase;
import org.opentosca.toscana.core.plugin.lifecycle.TransformationLifecycle;
import org.opentosca.toscana.core.plugin.lifecycle.ValidationFailureException;
import org.opentosca.toscana.core.transformation.TransformationContext;
import org.opentosca.toscana.core.transformation.platform.Platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 This class represents the base class for every Plugin that wants to implement the Transformation lifecycle

 @param <LifecycleT> The class type of the plugin specific implementation of the Lifecycle interface */
public abstract class TOSCAnaPlugin<LifecycleT extends TransformationLifecycle> {

    /**
     Immutable list of execution tasks, they are in the right execution order and get executed from the first
     index to the last
     */
    private final List<ExecutionPhase<LifecycleT>> executionPhases;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Platform platform;

    /**
     Initializes the plugin, that means that all tasks get added to a internal list (executionPhases) that then gets
     executed by the transform method.
     <p>
     The tasks get stored in a list (environment tasks)
     */
    public TOSCAnaPlugin(Platform platform) {
        this.platform = Objects.requireNonNull(platform, "The platform is not allowed to be null");
        this.executionPhases = populateExecutionPhases();
        initPlugin();
    }

    private List<ExecutionPhase<LifecycleT>> populateExecutionPhases() {
        List<ExecutionPhase<LifecycleT>> executionPhases = new ArrayList<>();

        //Environment validation
        executionPhases.add(new ExecutionPhase<>("check environment", (e) -> {
            if (!checkEnvironment()) {
                throw new ValidationFailureException("Transformation Failed," +
                    " because the Environment check has failed!");
            }
        }));
        //Model validation
        executionPhases.add(new ExecutionPhase<>("check model", (e) -> {
            if (!e.checkModel()) {
                throw new ValidationFailureException("Transformation Failed," +
                    " because the model check has failed!");
            }
        }));
        //Transformation phases
        executionPhases.add(new ExecutionPhase<>("prepare", TransformationLifecycle::prepare));
        executionPhases.add(new ExecutionPhase<>("transformation", TransformationLifecycle::transform));
        executionPhases.add(new ExecutionPhase<>("cleanup", TransformationLifecycle::cleanup));
        // Add Deployment Phase
        if (platform.supportsDeployment) {
            executionPhases.add(new ExecutionPhase<>(
                "deploy",
                TransformationLifecycle::deploy,
                TransformationContext::performDeployment
            ));
        }
        return Collections.unmodifiableList(executionPhases);
    }

    private void initPlugin() {
        logger.info("Initializing plugin '{}'", this.platform.name);
        this.init();
        logger.info("Initialized plugin '{}'", this.platform.name);
    }

    /**
     Is called during the initialisation of the plugin
     */
    protected void init() {
        //Empty method, allows plugin to run code on initialisation
    }

    public Platform getPlatform() {
        return platform;
    }

    /**
     This method will transform  given model (contained in the TransformationContext instance) and will store the result
     in a directory provided by the context.
     <p>
     Performs the execution in phases in the order that has been defined during the construction of the object

     @param context context for the transformation
     */
    public void transform(TransformationContext context) throws Exception {
        Logger logger = context.getLogger(getClass());

        //Store current time for time measurement
        long time = System.currentTimeMillis();

        logger.info("Building Lifecycle interface");
        LifecycleT lifecycleInterface = getInstance(context);

        int taskCount = countExecutionPhases(context);

        logger.info("This transformation has {} phases", taskCount);
        for (int i = 0; i < executionPhases.size(); i++) {
            ExecutionPhase<LifecycleT> phase = executionPhases.get(i);
            if (phase.shouldExecute(context)) {
                logger.info("Executing phase '{}' ({} of {})", phase.getName(), (i + 1), taskCount);
                phase.execute(lifecycleInterface);
            } else {
                phase.skip();
                logger.info("Skipping phase '{}' ({} of {})", phase.getName(), (i + 1), taskCount);
            }
        }
        time = System.currentTimeMillis() - time;
        logger.info("Transformation finished after {} ms", time);
    }

    private int countExecutionPhases(TransformationContext ctx) {
        return (int) this.executionPhases.stream().filter(e -> e.shouldExecute(ctx)).count();
    }

    /**
     Checks if all required environment parameters like installed CLIs, running services (such as docker) are available
     if so the method will return true, false otherwise (results in failure of the transformation)

     @return true if all env parameters are set, false otherwise
     */
    protected boolean checkEnvironment() {
        return true;
    }

    /**
     @param context The transformation context for which the Lifecycle interface should be built
     @return a newly constructed instance of the LifecycleInterface implemented by this plugin.
     */
    protected abstract LifecycleT getInstance(TransformationContext context) throws Exception;

    public List<ExecutionPhase<LifecycleT>> getExecutionPhases() {
        return executionPhases;
    }
}
