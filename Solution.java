import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.io.IOException;

public class Solution {

	static Scanner sc = new Scanner(System.in);
	static File file = new File("test1output.txt"); 
	public static void main(String[] args) throws IOException {


		FileWriter writer = new FileWriter(file,true);;



		int inputNum = sc.nextInt();
		TwoThreeTree planetTree = new TwoThreeTree();

		// Reads in input and constructs tree using insert() method
		for(int i=0; i<inputNum; i++)
		{
			String name =sc.next();
			int fee = sc.nextInt();
			twothree.insert(name, fee, planetTree);
		}

		String planet1 ="";
		String planet2 ="";
		inputNum = sc.nextInt();
		for(int i=0; i<inputNum; i++)
		{

			planet1=sc.next();
			planet2 =sc.next();
			printRange(planet1, planet2, planetTree,writer);

		}

		writer.close();


	}
	public static void printAll(Node p,int h, FileWriter writer) throws IOException
	{
		if(h!=0)
		{

			InternalNode internalP = (InternalNode) p;
			if(internalP.child2 == null)
			{
				printAll(internalP.child0, h-1, writer);
				printAll(internalP.child1,h-1,writer);
			}
			if(internalP.child2 != null)
			{
				printAll(internalP.child0, h-1,writer);
				printAll(internalP.child1,h-1,writer);
				printAll(internalP.child2, h-1,writer);
			}
		}
		if(h==0)
		{


			LeafNode leafp = (LeafNode)p;
			try {
				writer.write(p.guide + " " + leafp.value + "\n");
			} catch (IOException e) {
				e.printStackTrace(); // I'd rather declare method with throws IOException and omit this catch.
			} 
		}
	}
	static void printRange(String x, String y, TwoThreeTree planetTree, FileWriter writer) throws IOException
	{


		// if database is empty
		if(planetTree.root==null)
		{

		
			return;
		}
		//set x as the lower string
		Node[] pathToX = null;
		Node[] pathToY=null;
		if(x.compareTo(y)>0)
		{
			String temp = y;
			y=x;
			x=temp;
		}

		//return node[] using the search method
		pathToX = search(x, planetTree );
		pathToY = search(y, planetTree);


		//find the divergence point, should be the same for both node[]
		int divergence =0;

		for(int i =0; i<pathToX.length; i++)
		{
			if(pathToX[i] == pathToY[i])
			{
				divergence =i;
			}
		}
		//if twoThreeTree has 1-3 nodes
		if(planetTree.height==1)
		{
			pathToX = search(x, planetTree);
			pathToY = search(y, planetTree);
			//if x and y are the same and the root of the tree
			if(planetTree.root.guide.equals(x) && planetTree.root.guide.equals(y))
			{

				printAll(planetTree.root,0,writer);
			}
			// if twoThreeTree has a child2 but child2 isnt y
			else if(((InternalNode)planetTree.root).child2 != null && pathToY[1] != ((InternalNode)planetTree.root).child2)
			{
				printAll(((InternalNode)planetTree.root).child0,0,writer);
				printAll(((InternalNode)planetTree.root).child1,0,writer);
			}
			// if child 1 is x and child 2 is y
			else if(((InternalNode)planetTree.root).child0 != null && pathToX[1] != ((InternalNode)planetTree.root).child0)
			{
				printAll(((InternalNode)planetTree.root).child1,0,writer);
				printAll(((InternalNode)planetTree.root).child2,0,writer);
			}
			// print the whole tree
			else
			{  
				printAll(planetTree.root,1,writer);
			}


		}
		// the entire range of the query is not in the tree if there is no divergence therefore if there is no divergence check if the key is in the tree if it is print if not return

		else 
		{   

			if(pathToX[pathToX.length-1] == pathToY[pathToY.length-1])
			{
				if(isInTree(x,planetTree)!= null)
				{
					try {
						writer.write(pathToX[pathToX.length-1].guide + " "+ ((LeafNode)pathToX[pathToX.length-1]).value + "\n");
					} catch (IOException e) {
						e.printStackTrace(); 
					} 
				}

				return;
			}
			if(pathToX[pathToX.length-1].guide.compareTo(x) >= 0)
			{

				//print x
				try {
					writer.write(pathToX[pathToX.length-1].guide + " "+ ((LeafNode)pathToX[pathToX.length-1]).value + "\n");
				} catch (IOException e) {
					e.printStackTrace(); 
				} 
			}
			//walk back along pathToX
			for(int i = pathToX.length-2; i>=divergence; i--)
			{
				//if for loop hit the divergence and there is a middle node, printAll the middle node
				if(i == divergence)
				{            

					if(pathToX[i+1] == ((InternalNode)pathToX[i]).child0 && ((InternalNode)pathToX[i]).child2 == pathToY[divergence+1] && ((InternalNode)pathToX[i]).child1 != null )
					{
						printAll(((InternalNode)pathToX[i]).child1,  pathToX.length -i-2,writer );
					}
				}
				else if(((InternalNode)pathToX[i]).child2 != null && ((InternalNode)pathToX[i]).child0 == pathToX[i+1] && i> divergence)
				{
					printAll(((InternalNode)pathToX[i]).child1, pathToX.length -i-2,writer );
					printAll(((InternalNode)pathToX[i]).child2, pathToX.length -i-2,writer ); 
				}
				else if(((InternalNode)pathToX[i]).child2 != null && ((InternalNode)pathToX[i]).child1 == pathToX[i+1]&& i> divergence)
				{
					printAll(((InternalNode)pathToX[i]).child2, pathToX.length -i-2,writer );
				}
				else if(((InternalNode)pathToX[i]).child2 == null && ((InternalNode)pathToX[i]).child0 == pathToX[i+1]&& i> divergence)
				{    
					printAll(((InternalNode)pathToX[i]).child1, pathToX.length -i-2,writer );
				}


			}






			for(int i = divergence+1; i<pathToY.length-1; i++)
			{

				if(((InternalNode)pathToY[i]).child2 == pathToY[i+1])
				{

					printAll(((InternalNode)pathToY[i]).child0, pathToY.length-i-2,writer);
					printAll(((InternalNode)pathToY[i]).child1, pathToY.length-i-2,writer);

				}
				else if(((InternalNode)pathToY[i]).child1 == pathToY[i+1])
				{
					printAll(((InternalNode)pathToY[i]).child0, pathToY.length-i-2,writer);

				}




			}            
			if(pathToY[pathToY.length-1].guide.compareTo(y) <= 0)
			{
				try {
					writer.write(pathToY[pathToY.length-1].guide + " " + ((LeafNode)pathToY[pathToY.length-1]).value + "\n");
				} catch (IOException e) {
					e.printStackTrace(); 
				} 
			}
		}
	}





	public static Node[] search(String x, TwoThreeTree searchTree)
	{
		Node[] path = new Node[searchTree.height+1];
		Node root = searchTree.root;
		int height = searchTree.height;
		Node searchNode = root;
		for(int i =0; i<height; i++)
		{
			if(x.compareTo(((InternalNode)searchNode).child0.guide) <=0)
			{
				path[i] = searchNode;
				searchNode = ((InternalNode)searchNode).child0;
			}
			else if(((InternalNode)searchNode).child2 == null || x.compareTo(((InternalNode)searchNode).child1.guide) <= 0)
			{
				path[i] = searchNode;
				searchNode = ((InternalNode)searchNode).child1;
			}

			else
			{
				path[i]=searchNode;
				searchNode = ((InternalNode)searchNode).child2;
			}
		}
		path[height] = searchNode;
		return path;
	}
	public static Node[] isInTree(String x, TwoThreeTree searchTree)
	{
		Node[] path = new Node[searchTree.height+1];
		Node root = searchTree.root;
		int height = searchTree.height;
		Node searchNode = root;
		for(int i =0; i<height; i++)
		{
			if(x.compareTo(((InternalNode)searchNode).child0.guide) <=0)
			{
				path[i] = searchNode;
				searchNode = ((InternalNode)searchNode).child0;
			}
			else if(((InternalNode)searchNode).child2 == null || x.compareTo(((InternalNode)searchNode).child1.guide) <= 0)
			{
				path[i] = searchNode;
				searchNode = ((InternalNode)searchNode).child1;
			}

			else
			{
				path[i]=searchNode;
				searchNode = ((InternalNode)searchNode).child2;
			}
		}
		if(searchNode.guide.equals(x))
		{
			path[height] = searchNode;
			return path;
		}
		else
			return null;
	}

}
