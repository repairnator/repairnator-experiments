package org.jboss.resteasy.core;

import org.jboss.resteasy.plugins.providers.validation.ViolationsContainer;
import org.jboss.resteasy.resteasy_jaxrs.i18n.Messages;
import org.jboss.resteasy.spi.ApplicationException;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.jboss.resteasy.spi.MethodInjector;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.metadata.MethodParameter;
import org.jboss.resteasy.spi.metadata.ResourceLocator;
import org.jboss.resteasy.spi.validation.GeneralValidator;
import org.jboss.resteasy.spi.validation.GeneralValidatorCDI;

import javax.ws.rs.WebApplicationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class MethodInjectorImpl implements MethodInjector
{
   protected ValueInjector[] params;
   protected ResteasyProviderFactory factory;
   protected ResourceLocator method;
   protected Method interfaceBasedMethod;
   protected boolean expectsBody;

   public MethodInjectorImpl(ResourceLocator resourceMethod, ResteasyProviderFactory factory)
   {
      this.factory = factory;
      this.method = resourceMethod;
      this.interfaceBasedMethod = findInterfaceBasedMethod(resourceMethod.getResourceClass().getClazz(), resourceMethod.getMethod());
      params = new ValueInjector[resourceMethod.getParams().length];
      int i = 0;
      for (MethodParameter parameter : resourceMethod.getParams())
      {
         params[i] = factory.getInjectorFactory().createParameterExtractor(parameter, factory);
         if (params[i] instanceof MessageBodyParameterInjector) expectsBody = true;
         i++;
      }
   }

   @Override
   public boolean expectsBody()
   {
      return expectsBody;
   }

   @SuppressWarnings(value = "unchecked")
   public static Method findInterfaceBasedMethod(Class root, Method method)
   {
      if (method.getDeclaringClass().isInterface() || root.isInterface()) return method;

      for (Class intf : root.getInterfaces())
      {
         try
         {
            return intf.getMethod(method.getName(), method.getParameterTypes());
         }
         catch (NoSuchMethodException ignored)
         {}
      }

      if (root.getSuperclass() == null || root.getSuperclass().equals(Object.class)) return method;
      return findInterfaceBasedMethod(root.getSuperclass(), method);

   }

   public ValueInjector[] getParams()
   {
      return params;
   }

   @Override
   public CompletionStage<Object[]> injectArguments(HttpRequest input, HttpResponse response)
   {
      try
      {
         if (params != null && params.length > 0)
         {
            Object[] args = new Object[params.length];
            int i = 0;
            CompletionStage<Object> ret = CompletableFuture.completedFuture(null);
            for (ValueInjector extractor : params)
            {
               int j = i++;
               ret = ret.thenCompose(v -> extractor.inject(input, response, true)
                                       .thenApply(value -> args[j] = value));
            }
            return ret.thenApply(v -> args);
         }
         else
            return CompletableFuture.completedFuture(null);
      }
      catch (WebApplicationException we)
      {
         throw we;
      }
      catch (Failure f)
      {
         throw f;
      }
      catch (Exception e)
      {
         BadRequestException badRequest = new BadRequestException(Messages.MESSAGES.failedProcessingArguments(method.toString()), e);
         badRequest.setLoggable(true);
         throw badRequest;
      }
   }

   public CompletionStage<Object> invoke(HttpRequest request, HttpResponse httpResponse, Object resource) throws Failure, ApplicationException
   {
      return injectArguments(request, httpResponse)
            .thenApply(args -> invoke(request, httpResponse, resource, args));
   }

   private Object invoke(HttpRequest request, HttpResponse httpResponse, Object resource, Object[] args)
   {
      GeneralValidator validator = GeneralValidator.class.cast(request.getAttribute(GeneralValidator.class.getName()));
      if (validator != null)
      {
         validator.validateAllParameters(request, resource, method.getMethod(), args);
      }

      Method invokedMethod = method.getMethod();
      if (!invokedMethod.getDeclaringClass().isAssignableFrom(resource.getClass()))
      {
         // invokedMethod is for when the target object might be a proxy and
         // resteasy is getting the bean class to introspect.
         // In other words ResourceMethod.getMethod() does not have the same declared class as the proxy:
         // An example is a proxied Spring bean that is a resource
         // interface ProxiedInterface { String get(); }
         // @Path("resource") class MyResource implements ProxiedInterface {
         //     @GET String get() {...}
         // }
         //
         invokedMethod = interfaceBasedMethod;
      }

      Object result = null;
      try
      {
         result = invokedMethod.invoke(resource, args);
      }
      catch (IllegalAccessException e)
      {
         throw new InternalServerErrorException(Messages.MESSAGES.notAllowedToReflectOnMethod(method.toString()), e);
      }
      catch (InvocationTargetException e)
      {
         Throwable cause = e.getCause();
         if (validator instanceof GeneralValidatorCDI)
         {
            GeneralValidatorCDI.class.cast(validator).checkForConstraintViolations(request, e);
         }
         throw new ApplicationException(cause);
      }
      catch (IllegalArgumentException e)
      {
         String msg = Messages.MESSAGES.badArguments(method.toString() + "  (");
         if (args != null)
         {
            boolean first = false;
            for (Object arg : args)
            {
               if (!first)
               {
                  first = true;
               }
               else
               {
                  msg += ",";
               }
               if (arg == null)
               {
                  msg += " null";
                  continue;
               }
               msg += " " + arg.getClass().getName() + " " + arg;
            }
         }
         msg += " )";
         throw new InternalServerErrorException(msg, e);
      }
      if (validator != null)
      {
         validator.validateReturnValue(request, resource, method.getMethod(), result);
      }
      return result;
   }

}
