package io.scalecube.examples.services;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;

import java.util.concurrent.CompletableFuture;

@Service
public interface GreetingService {

  @ServiceMethod
  CompletableFuture<String> greeting(String string);

  @ServiceMethod
  CompletableFuture<String> greetingException(String name);

  @ServiceMethod
  CompletableFuture<GreetingResponse> greetingRequest(GreetingRequest request);

}
