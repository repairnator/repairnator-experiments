package edu.uci.ics.texera.textql.statements;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

import edu.uci.ics.texera.dataflow.common.PredicateBase;
import edu.uci.ics.texera.dataflow.plangen.OperatorLink;

/**
 * Statement class and subclasses(SelectExtractStatement, CreateViewStatement)
 * Each Statement class has an ID. Subclasses of Statements have specific
 * fields related to its function.
 * Statement --+ SelectExtractStatement
 *             + CreateViewStatement
 *             
 * @author Flavio Bayer
 * 
 */
public abstract class Statement {
    
    /**
     * The { @code String } identifier of each Statement object.
     */
    private String id;
    
    /**
     * Create a { @code Statement } with all the parameters set to { @code null }.
     */
    public Statement() {

    }
    
    
    /**
     * Create a { @code Statement } with the given ID.
     * @param id The ID of this statement.
     */
    public Statement(String id) {
      this.id = id;
    }
    
    
    /**
     * Get the ID of the statement.
     * @return The ID of the statement.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Set the ID of the statement.
     * @param id The new ID of the statement.
     */
    public void setId(String id) {
        this.id = id;
    }
    
    
    /**
     * Return the ID of the "first" node in the statement that is connected to the previous statement.
     * @return The ID to be used as input of this operator by a { @code LinkBean }.
     */
    public abstract String getInputNodeID();
    
    /**
     * Return the ID of the "last" node in the statement that is connected to the next statement.
     * @return The ID to be used as output of this operator by a { @code LinkBean }.
     */
    public abstract String getOutputNodeID();
        
    /**
     * Return a list of operators generated when this statement is converted to beans.
     * @return The list of operator beans generated by this statement.
     */
    public abstract List<PredicateBase> getInternalOperatorBeans();

    /**
     * Return a list of links generated when this statement is converted to beans.
     * @return The list of link beans generated by this statement.
     */
    public abstract List<OperatorLink> getInternalLinkBeans();
    
    /**
     * Return a list of IDs of operators required by this statement (the dependencies of this Statement)
     * when converted to beans.
     * @return A list with the IDs of required Statements.
     */
    public abstract List<String> getInputViews();
    
    
    @Override
    public boolean equals(Object other) {
      if (other == null) { return false; }
      if (other.getClass() != this.getClass()) { return false; }
      Statement statement = (Statement) other;
      return new EqualsBuilder()
                  .append(id, statement.id)
                  .isEquals();
    }
    
}