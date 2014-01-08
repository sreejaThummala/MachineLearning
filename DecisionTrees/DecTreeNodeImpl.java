import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class DecTreeNodeImpl extends DecTreeNode {
	
	private DecTreeNodeImpl parent;
	private List<Instance> examples;
	private int nodeID;

	public DecTreeNodeImpl(){
		super(null,null,null,false);
		children = new LinkedList<DecTreeNode>();
	}
	
	public boolean  isLeaf(){
		return terminal;
	}
	
	public boolean isRoot(){
		return (parentAttributeValue.equals("ROOT"));
	}
	
	public void setNodeRoot(boolean isRoot){
		if(isRoot){
			parentAttributeValue = "ROOT";
		}
	}
	
	public void setNodeLeaf(boolean isLeaf){
		this.terminal = isLeaf;
		if(isLeaf){
			this.children = null;
		}
	}
	
	public void setParent(DecTreeNodeImpl parent){
		
		this.parent = parent;
	}
	
	public List<DecTreeNode> getChildren(){
		return children;
	}
	
	public void setAttribute(String attribute){
		this.attribute = attribute;
	}
	
	public String getAttribute(){
		return attribute;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}

	public void setParentAttributeValue(String possibleAttribute) {
		this.parentAttributeValue = possibleAttribute;
		
	}
	
	public DecTreeNodeImpl getParent(){
		return parent;
	}
	
	public void setNodeID(DecTreeNodeImpl parent){
		this.parent = parent;
	}

	public List<Instance> getExamples(){
		return examples;
	}
	
	public int getNodeID(){
		return nodeID;
	}
	
	public void setNodeID(int nodeID){
		this.nodeID = nodeID;
	}
	
	public void setExamples(List<Instance> examples){
		this.examples = examples;
	}
	
	@Override
	public boolean equals(Object obj) {
		DecTreeNodeImpl other = (DecTreeNodeImpl)obj;
		return other.getNodeID() == this.getNodeID();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		DecTreeNodeImpl clone = new DecTreeNodeImpl();
		clone.setAttribute(this.attribute);
		clone.setLabel(this.label);
		clone.setParent(this.parent);
		clone.setNodeLeaf(this.terminal);
		clone.setParentAttributeValue(this.parentAttributeValue);
		List<DecTreeNode> children = this.children;
		
		if(children!=null&&children.size()!=0){
			for(int i=0;i<children.size();i++){
				DecTreeNode childClone = (DecTreeNodeImpl)((DecTreeNodeImpl)children.get(i)).clone();
				clone.addChild(childClone);
			}
		}
	
		return clone;
	}


}
