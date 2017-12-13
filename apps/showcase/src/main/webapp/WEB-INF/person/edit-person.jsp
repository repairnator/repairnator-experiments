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
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
	<title>Struts2 Showcase - Person Manager Example</title>
</head>
<body>
<div class="page-header">
	<h1>Person Manager Example</h1>
</div>

<div class="container-fluid">
	<div class="row">
		<div class="col-md-3">
			<ul class="nav nav-tabs nav-stacked">
				<s:url var="listpeopleurl" action="list-people" namespace="/person" />
				<li><s:a href="%{listpeopleurl}">List all people</s:a> </li>
				<s:url var="editpersonurl" action="edit-person" namespace="/person" />
				<li class="active"><s:a href="%{editpersonurl}">Edit people</s:a></li>
				<s:url var="newpersonurl" action="new-person" namespace="/person" method="input"/>
				<li><s:a href="%{newpersonurl}">Create a new person</s:a></li>
			</ul>
		</div>
		<div class="col-md-9">
			<s:form action="edit-person" theme="simple" validate="false">

				<table class="table table-striped table-bordered table-hover table-condensed">
					<tr>
						<th>ID</th>
						<th>First Name</th>
						<th>Last Name</th>
					</tr>
					<s:iterator var="p" value="persons">
						<tr>
							<td>
								<s:property value="%{id}" />
							</td>
							<td>
								<s:textfield label="First Name" name="persons(%{id}).name" value="%{name}" theme="simple" />
							</td>
							<td>
								<s:textfield label="Last Name" name="persons(%{id}).lastName" value="%{lastName}" theme="simple"/>
							</td>
						</tr>
					</s:iterator>
				</table>

				<s:submit method="save" value="Save all persons" cssClass="btn btn-primary"/>
			</s:form>
		</div>
	</div>
</div>
</body>
</html>
