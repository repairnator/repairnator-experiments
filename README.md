# Mailosaur Java Client Library

[Mailosaur](https://mailosaur.com) allows you to automate tests involving email. Allowing you to perform end-to-end automated and functional email testing.

[![Build Status](https://travis-ci.org/mailosaur/mailosaur-java.svg?branch=master)](https://travis-ci.org/mailosaur/mailosaur-java)

## Installation

### Maven users

Add this dependency to your project's POM:

```
<dependency>
  <groupId>com.mailosaur</groupId>
  <artifactId>mailosaur-java</artifactId>
  <version>5.0.0</version>
</dependency>
```

### Others

You'll need to manually install the following JARs:

* The Mailosaur JAR from https://github.com/mailosaur/mailosaur-java/releases/latest
* Something else from https://

## Documentation and usage examples

[Mailosaur's documentation](https://mailosaur.com/docs) includes all the information and usage examples you'll need.

## Building

1. Install [Node.js](https://nodejs.org/) (LTS)

2. Install [AutoRest](https://github.com/Azure/autorest) using `npm`

```
# Depending on your configuration you may need to be elevated or root to run this. (on OSX/Linux use 'sudo')
npm install -g autorest
```

3. Run the build script

```
./build.sh
```

### AutoRest Configuration

This project uses [AutoRest](https://github.com/Azure/autorest), below is the configuration that the `autorest` command will automatically pick up.

> see https://aka.ms/autorest

```yaml
input-file: https://next.mailosaur.com/swagger/v4/swagger.json
```

```yaml
java:
    output-folder: src/main/java/com/mailosaur
    add-credentials: true
    sync-methods: essential 
    use-internal-contructors: true
    override-client-name: MailosaurBaseClient
    namespace: com.mailosaur
```

## Running tests

Once you've cloned this repository locally, you can simply run:

```
export MAILOSAUR_API_KEY=your_api_key
export MAILOSAUR_SERVER=server_id

mvn test
```

## Contacting us

You can get us at [support@mailosaur.com](mailto:support@mailosaur.com)

## License

Copyright (c) 2018 Mailosaur Ltd
Distributed under MIT license.