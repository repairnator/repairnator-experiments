<!--
/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
-->
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
	<title>Struts2 Showcase - Model Driven Example</title>
	<s:head/>
</head>

<body>

<div class="page-header">
	<h1>Model Driven Example</h1>
</div>

<div class="container-fluid">
	<div class="row">
		<div class="col-md-12">

			<s:form action="modelDrivenResult" method="POST" namespace="/modelDriven">

				<s:textfield
						label="Gangster Name"
						name="name"/>
				<s:textfield
						label="Gangster Age"
						name="age"/>
				<s:checkbox
						label="Gangster Busted Before"
						name="bustedBefore"/>
				<s:textarea
						cols="30"
						rows="5"
						label="Gangster Description"
						name="description"/>
				<s:submit cssClass="btn btn-primary"/>

			</s:form>

		</div>
	</div>
</div>
</body>
</html>