--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- insert 3 lines
/**/
insert into something(id, name) values (1, 'one');
# line 2
/*update*/insert into something (id, name) values (2, 'two');
/*insert into something (id, name) values (2, 'two');*/
/* test
insert into something (id, name) values (2, 'two');

*/insert into /*something*/ something (id, name) values (3, 'three');
// line 3
