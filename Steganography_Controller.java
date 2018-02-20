 



import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;

/*
 *Steganography_Controller Class
 */
public class Steganography_Controller
{
	
    private Steganography_View	view;
    private Steganography		model;

	
	private JPanel		decode_panel;
	private JPanel		encode_panel;
	
	private JTextArea 	input;
	private JButton		encodeButton,decodeButton;
	private JLabel		image_input;
	
	private JMenuItem 	encode;
	private JMenuItem 	decode;
	private JMenuItem 	exit;

	
	private Encode			enc;
	private Decode			dec;
	private EncodeButton	encButton;
	private DecodeButton	decButton;

	
	private String			stat_path = "";
	private String			stat_name = "";

	
	public Steganography_Controller(Steganography_View aView, Steganography aModel)
	{
		
		view  = aView;
		model = aModel;

		
		encode_panel	= view.getTextPanel();
		decode_panel	= view.getImagePanel();
		
		input			= view.getText();
		image_input		= view.getImageInput();
		
		encodeButton	= view.getEButton();
		decodeButton	= view.getDButton();
		
		encode			= view.getEncode();
		decode			= view.getDecode();
		exit			= view.getExit();

		
		enc = new Encode();
		encode.addActionListener(enc);
		dec = new Decode();
		decode.addActionListener(dec);
		exit.addActionListener(new Exit());
		encButton = new EncodeButton();
		encodeButton.addActionListener(encButton);
		decButton = new DecodeButton();
		decodeButton.addActionListener(decButton);

		
		encode_view();
	}

	
	private void encode_view()
	{
		update();
		view.setContentPane(encode_panel);
		view.setVisible(true);
	}

	
	private void decode_view()
	{
		update();
		view.setContentPane(decode_panel);
		view.setVisible(true);
	}

	
	private class Encode implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{
			encode_view();
		}
	}

	
	private class Decode implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{
			decode_view(); 

			
			JFileChooser chooser = new JFileChooser("./");
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setFileFilter(new Image_Filter());
			int returnVal = chooser.showOpenDialog(view);
			if (returnVal == JFileChooser.APPROVE_OPTION){
				File directory = chooser.getSelectedFile();
				try{
					String image = directory.getPath();
					stat_name = directory.getName();
					stat_path = directory.getPath();
					stat_path = stat_path.substring(0,stat_path.length()-stat_name.length()-1);
					stat_name = stat_name.substring(0, stat_name.length()-4);
					image_input.setIcon(new ImageIcon(ImageIO.read(new File(image))));
				}
				catch(Exception except) {
					
					JOptionPane.showMessageDialog(view, "The File cannot be opened!", 
							"Error!", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}

	
	private class Exit implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0); 
		}
	}

	
	private class EncodeButton implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{
			
			JFileChooser chooser = new JFileChooser("./");
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setFileFilter(new Image_Filter());
			int returnVal = chooser.showOpenDialog(view);
			if (returnVal == JFileChooser.APPROVE_OPTION){
				File directory = chooser.getSelectedFile();
				try{
					String text = input.getText();
					String ext  = Image_Filter.getExtension(directory);
					String name = directory.getName();
					String path = directory.getPath();
					path = path.substring(0,path.length()-name.length()-1);
					name = name.substring(0, name.length()-4);

					String stegan = JOptionPane.showInputDialog(view,
							"Enter output file name:", "File name",
							JOptionPane.PLAIN_MESSAGE);

					if(model.encode(path,name,ext,stegan,text))
					{
						JOptionPane.showMessageDialog(view, "The Image was encoded Successfully!", 
								"Success!", JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{
						JOptionPane.showMessageDialog(view, "The Image could not be encoded!", 
								"Error!", JOptionPane.INFORMATION_MESSAGE);
					}
					
					decode_view();
					image_input.setIcon(new ImageIcon(ImageIO.read(new File(path + "/" + stegan + ".png"))));
				}
				catch(Exception except) {
					
					JOptionPane.showMessageDialog(view, "The File cannot be opened!", 
							"Error!", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}

	}

	
	private class DecodeButton implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{
			String message = model.decode(stat_path, stat_name);
			System.out.println(stat_path + ", " + stat_name);
			if(message != "")
			{
				encode_view();
				JOptionPane.showMessageDialog(view, "The Image was decoded Successfully!", 
						"Success!", JOptionPane.INFORMATION_MESSAGE);
				input.setText(message);
			}
			else
			{
				JOptionPane.showMessageDialog(view, "The Image could not be decoded!", 
						"Error!", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	
	public void update()
	{
		input.setText("");			
		image_input.setIcon(null);	
		stat_path = "";				
		stat_name = "";				
	}

	
	public static void main(String args[])
	{
		new Steganography_Controller(
				new Steganography_View("Steganography"),
				new Steganography()
		);
	}
}








/*
 *Class Steganography_View
 */
 class Steganography_View extends JFrame
{
	
    
	private static int WIDTH  = 700;
	private static int HEIGHT = 600;

	
	private JTextArea 	input;
	private JScrollBar 	scroll,scroll2;
	private JButton		encodeButton,decodeButton;
	private JLabel		image_input;

	
	private JMenu 		file;
	private JMenuItem 	encode;
	private JMenuItem 	decode;
	private JMenuItem 	exit;

	
	public Steganography_View(String name)
	{
		
		super(name);

		
		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu("File");	file.setMnemonic('F');
		encode = new JMenuItem("Encode"); encode.setMnemonic('E'); file.add(encode);
		decode = new JMenuItem("Decode"); decode.setMnemonic('D'); file.add(decode);
		file.addSeparator();
		exit = new JMenuItem("Exit"); exit.setMnemonic('x'); file.add(exit);

		menu.add(file);
		setJMenuBar(menu);

		
		setResizable(true);						
		setBackground(Color.blue);			
		setLocation(100,100);					
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WIDTH,HEIGHT);					
		setVisible(true);						
	}

	
	public JMenuItem	getEncode()		{ return encode;			}
	
	public JMenuItem	getDecode()		{ return decode;			}
	
	public JMenuItem	getExit()		{ return exit;				}
	
	public JTextArea	getText()		{ return input;				}
	
	public JLabel		getImageInput()	{ return image_input;		}
	
	public JPanel		getTextPanel()	{ return new Text_Panel();	}
	
	public JPanel		getImagePanel()	{ return new Image_Panel();	}
	
	public JButton		getEButton()	{ return encodeButton;		}
	
	public JButton		getDButton()	{ return decodeButton;		}

	/*
	 *Class Text_Panel
	 */
	private class Text_Panel extends JPanel
	{
		
		public Text_Panel()
		{
			
			GridBagLayout layout = new GridBagLayout(); 
			GridBagConstraints layoutConstraints = new GridBagConstraints(); 
			setLayout(layout);

			input = new JTextArea();
			layoutConstraints.gridx 	= 0; layoutConstraints.gridy = 0; 
			layoutConstraints.gridwidth = 1; layoutConstraints.gridheight = 1; 
			layoutConstraints.fill 		= GridBagConstraints.BOTH; 
			layoutConstraints.insets 	= new Insets(0,0,0,0); 
			layoutConstraints.anchor 	= GridBagConstraints.PAGE_END; 
			layoutConstraints.weightx 	= 1.0; layoutConstraints.weighty = 50.0;
			JScrollPane scroll = new JScrollPane(input,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
			layout.setConstraints(scroll,layoutConstraints);
			scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
			add(scroll);

			encodeButton = new JButton("Encode Now");
			layoutConstraints.gridx 	= 0; layoutConstraints.gridy = 1; 
			layoutConstraints.gridwidth = 1; layoutConstraints.gridheight = 1; 
			layoutConstraints.fill 		= GridBagConstraints.VERTICAL; 
			layoutConstraints.insets 	= new Insets(0,-5,-5,-5); 
			layoutConstraints.anchor 	= GridBagConstraints.PAGE_END; 
			layoutConstraints.weightx 	= 1.0; layoutConstraints.weighty = 1.0;
			layout.setConstraints(encodeButton,layoutConstraints);
			add(encodeButton);

			
			setBackground(Color.lightGray);
			setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
		}
	}

	/*
	 *Class Image_Panel
	 */
	private class Image_Panel extends JPanel
	{
		
		public Image_Panel()
		{
			
			GridBagLayout layout = new GridBagLayout(); 
			GridBagConstraints layoutConstraints = new GridBagConstraints(); 
			setLayout(layout);

			image_input = new JLabel();
			layoutConstraints.gridx 	= 0; layoutConstraints.gridy = 0; 
			layoutConstraints.gridwidth = 1; layoutConstraints.gridheight = 1; 
			layoutConstraints.fill 		= GridBagConstraints.BOTH; 
			layoutConstraints.insets 	= new Insets(0,0,0,0); 
			layoutConstraints.anchor 	= GridBagConstraints.CENTER; 
			layoutConstraints.weightx 	= 1.0; layoutConstraints.weighty = 50.0;
			JScrollPane scroll2 = new JScrollPane(image_input,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
			layout.setConstraints(scroll2,layoutConstraints);
			scroll2.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
			image_input.setHorizontalAlignment(JLabel.CENTER);
			add(scroll2);

			decodeButton = new JButton("Decode Now");
			layoutConstraints.gridx 	= 0; layoutConstraints.gridy = 1; 
			layoutConstraints.gridwidth = 1; layoutConstraints.gridheight = 1; 
			layoutConstraints.fill 		= GridBagConstraints.BOTH; 
			layoutConstraints.insets 	= new Insets(0,-5,-5,-5); 
			layoutConstraints.anchor 	= GridBagConstraints.CENTER; 
			layoutConstraints.weightx 	= 1.0; layoutConstraints.weighty = 1.0;
			layout.setConstraints(decodeButton,layoutConstraints);
			add(decodeButton);

			
			setBackground(Color.lightGray);
			setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
		}
	}

	
	public static void main(String args[])
	{
		new Steganography_View("Steganography");
	}
}










/*
 *Class Steganography
 */
 class Steganography
{

	
	public Steganography()
	{
	}

	
	public boolean encode(String path, String original, String ext1, String stegan, String message)
	{
		String			file_name 	= image_path(path,original,ext1);
		BufferedImage 	image_orig	= getImage(file_name);

		
		BufferedImage image = user_space(image_orig);
		image = add_text(image,message);

		return(setImage(image,new File(image_path(path,stegan,"png")),"png"));
	}

	
	public String decode(String path, String name)
	{
		byte[] decode;
		try
		{
			
			BufferedImage image  = user_space(getImage(image_path(path,name,"png")));
			decode = decode_text(get_byte_data(image));
			return(new String(decode));
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
					"There is no hidden message in this image!","Error",
					JOptionPane.ERROR_MESSAGE);
			return "";
		}
	}

	
	private String image_path(String path, String name, String ext)
	{
		return path + "/" + name + "." + ext;
	}

	
	
	private BufferedImage getImage(String f)
	{
		BufferedImage 	image	= null;
		File 		file 	= new File(f);

		try
		{
			image = ImageIO.read(file);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, 
					"Image could not be read!","Error",JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}

	
	
	private boolean setImage(BufferedImage image, File file, String ext)
	{
		try
		{
			file.delete(); 
			ImageIO.write(image,ext,file);
			return true;
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
					"File could not be saved!","Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	
	
	private BufferedImage add_text(BufferedImage image, String text)
	{
		
		byte img[]  = get_byte_data(image);
		byte msg[] = text.getBytes();
		byte len[]   = bit_conversion(msg.length);
		try
		{
			encode_text(img, len,  0); 
			encode_text(img, msg, 32); 
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
					"Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}

	
	private BufferedImage user_space(BufferedImage image)
	{
		
		BufferedImage new_img  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D    graphics = new_img.createGraphics();
		graphics.drawRenderedImage(image, null);
		graphics.dispose(); 
		return new_img;
	}

	
	 
	private byte[] get_byte_data(BufferedImage image)
	{
		WritableRaster raster   = image.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}

	
	private byte[] bit_conversion(int i)
	{
		
		
		byte byte3 = (byte)((i & 0xFF000000) >>> 24); 
		byte byte2 = (byte)((i & 0x00FF0000) >>> 16); 
		byte byte1 = (byte)((i & 0x0000FF00) >>> 8 ); 
		byte byte0 = (byte)((i & 0x000000FF)       );
		
		return(new byte[]{byte3,byte2,byte1,byte0});
	}

	
	private byte[] encode_text(byte[] image, byte[] addition, int offset)
	{
		
		if(addition.length + offset > image.length)
		{
			throw new IllegalArgumentException("File not long enough!");
		}
		
		for(int i=0; i<addition.length; ++i)
		{
			
			int add = addition[i];
			for(int bit=7; bit>=0; --bit, ++offset) 
			{
				
				int b = (add >>> bit) & 1;
				
				image[offset] = (byte)((image[offset] & 0xFE) | b );
			}
		}
		return image;
	}

	
	private byte[] decode_text(byte[] image)
	{
		int length = 0;
		int offset  = 32;
		
		for(int i=0; i<32; ++i) 
		{
			length = (length << 1) | (image[i] & 1);
		}

		byte[] result = new byte[length];

		
		for(int b=0; b<result.length; ++b )
		{
			
			for(int i=0; i<8; ++i, ++offset)
			{
				
				result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
			}
		}
		return result;
	}
}







/*
 *Image_Filter Class
 */
 class Image_Filter extends javax.swing.filechooser.FileFilter
{
	
	protected boolean isImageFile(String ext)
	{
		return (ext.equals("jpg")||ext.equals("png"));
	}

	
	public boolean accept(File f)
	{
		if (f.isDirectory())
		{
			return true;
		}

		String extension = getExtension(f);
		if (extension.equals("jpg")||extension.equals("png"))
		{
			return true;
		}
		return false;
	}

	
	public String getDescription()
	{
		return "Supported Image Files";
	}

	
	protected static String getExtension(File f)
	{
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 &&  i < s.length() - 1) 
			return s.substring(i+1).toLowerCase();
		return "";
	}	
}
