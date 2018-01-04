package jsat.text;

import java.util.List;
import jsat.classifiers.CategoricalData;
import jsat.classifiers.ClassificationDataSet;
import jsat.text.tokenizer.Tokenizer;
import jsat.text.wordweighting.WordWeighting;
import jsat.utils.IntList;

/**
 * This class provides a framework for loading classification datasets made of 
 * text documents as vectors. This extension uses 
 * {@link #addOriginalDocument(java.lang.String, int) } instead so that the 
 * original documents have a class label associated with them. 
 * {@link #getDataSet() } then returns a classification data set, where the 
 * class label for each data point is the label provided when 
 * <tt>addOriginalDocument</tt> was called. 
 * <br>
 * New vectors created with {@link #newText(java.lang.String) } are inherently 
 * not part of the original data set, so do not need or receive a class label.
 * 
 * @author Edward Raff
 */
public abstract class ClassificationTextDataLoader extends TextDataLoader
{

    private static final long serialVersionUID = -3826551504785236576L;
    
    /**
     * The list of the true class labels for the data that was loaded before 
     * {@link #finishAdding() } was called. 
     */
    protected final List<Integer> classLabels;
    /**
     * The information about the class label that would be predicted for a 
     * classification data set.
     */
    protected CategoricalData labelInfo;
    
    
    /**
     * Creates a new text data loader
     * 
     * @param tokenizer the string tokenizer to use on each input
     * @param weighting the weighting scheme to apply to each vector in the 
     * collection
     */
    public ClassificationTextDataLoader(Tokenizer tokenizer, WordWeighting weighting)
    {
        super(tokenizer, weighting);
        classLabels = new IntList();
    }
    
    /**
     * The classification label data stored in {@link #labelInfo} must be set 
     * if the text loader is to return a classification data set. As such, this 
     * abstract class exists to force the user to set it, in this way they can 
     * not forget. <br>
     * This will be called in {@link #getDataSet() } just before 
     * {@link #initialLoad() } is called. 
     */
    protected abstract void setLabelInfo();

    /**
     * Should use {@link #addOriginalDocument(java.lang.String, int) } instead. 
     * @param text the text of the data to add
     * @return the index of the created document for the given text. Starts from
     * zero and counts up.
     */
    @Override
    protected int addOriginalDocument(String text)
    {
        throw new UnsupportedOperationException("addOriginalDocument(String"
                + " text, int label) should be used instead");
    }
    
    /**
     * To be called by the {@link #initialLoad() } method. 
     * It will take in the text and add a new document 
     * vector to the data set. Once all text documents 
     * have been loaded, this method should never be 
     * called again. <br>
     * This method is thread safe
     * 
     * @param text the text of the document to add
     * @param label the classification label for this document
     * @return the index of the created document for the given text. Starts from
     * zero and counts up.
     */
    protected int addOriginalDocument(String text, int label)
    {
        if(label >= labelInfo.getNumOfCategories())
            throw new RuntimeException("Invalid label given");
        int index = super.addOriginalDocument(text);
        synchronized(classLabels)
        {
            while(classLabels.size() < index)
                classLabels.add(-1);
            if(classLabels.size() ==  index)//we are where we expect
                classLabels.add(label);
            else//another thread beat us to the addition
                classLabels.set(index, label);
        }
        return index;
    }
    
    @Override
    public ClassificationDataSet getDataSet()
    {
        if(!noMoreAdding)
        {
            setLabelInfo();
            initialLoad();
            finishAdding();
        }
        
        ClassificationDataSet cds = 
                new ClassificationDataSet(vectors.get(0).length(), 
                new CategoricalData[]{}, labelInfo);
        for(int i = 0; i < vectors.size(); i++)
            cds.addDataPoint(vectors.get(i), new int[]{}, classLabels.get(i));
        
        return cds;
    }
}
