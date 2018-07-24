/*
 * Copyright 2017 Javier A. Ortiz Bultron javier.ortiz.78@gmail.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.validation.manager.core.db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
@Entity
@Table(name = "execution_result")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExecutionResult.findAll",
            query = "SELECT e FROM ExecutionResult e")
    , @NamedQuery(name = "ExecutionResult.findById",
            query = "SELECT e FROM ExecutionResult e WHERE e.id = :id")
    , @NamedQuery(name = "ExecutionResult.findByResultName",
            query = "SELECT e FROM ExecutionResult e WHERE e.resultName = :resultName")})
public class ExecutionResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "ExecutionResult_IDGEN")
    @TableGenerator(name = "ExecutionResult_IDGEN", table = "vm_id",
            pkColumnName = "table_name",
            valueColumnName = "last_id",
            pkColumnValue = "execution_result",
            initialValue = 1,
            allocationSize = 1)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "result_name")
    private String resultName;
    @OneToMany(mappedBy = "resultId")
    private List<ExecutionStep> executionStepList;

    public ExecutionResult() {
    }

    public ExecutionResult(String resultName) {
        this.resultName = resultName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResultName() {
        return resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    @XmlTransient
    @JsonIgnore
    public List<ExecutionStep> getExecutionStepList() {
        return executionStepList;
    }

    public void setExecutionStepList(List<ExecutionStep> executionStepList) {
        this.executionStepList = executionStepList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof ExecutionResult)) {
            return false;
        }
        ExecutionResult other = (ExecutionResult) object;
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "com.validation.manager.core.db.ExecutionResult[ id=" + id + " ]";
    }

}
