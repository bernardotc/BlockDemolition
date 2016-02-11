package pbgLecture5lab_wrapperForJBox2D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static pbgLecture5lab_wrapperForJBox2D.BasicPhysicsEngineUsingBox2D.WORLD_HEIGHT;
import static pbgLecture5lab_wrapperForJBox2D.BasicPhysicsEngineUsingBox2D.WORLD_WIDTH;


public class ThreadedGuiForPhysicsEngine {
	/* Author: Michael Fairbank
	 * Creation Date: 2016-01-28
	 * Significant changes applied:
	 */
	
	public ThreadedGuiForPhysicsEngine() {
	}

	private static JButton jButton_go;
	private static Thread theThread;
        private static JTextField angle;
        private static JTextField speed;
	public static void main(String[] args) throws Exception {
		BasicPhysicsEngineUsingBox2D game = new BasicPhysicsEngineUsingBox2D ();
		final BasicView view = new BasicView(game);
		JComponent mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(view, BorderLayout.CENTER);
		JPanel sidePanel=new JPanel();
		sidePanel.setLayout(new FlowLayout());
		jButton_go=new JButton("Go");
                angle = new JTextField("25");
                speed = new JTextField("20");
		sidePanel.add(jButton_go);
                sidePanel.add(angle);
                sidePanel.add(speed);
		mainPanel.add(sidePanel, BorderLayout.WEST);
		// add any new buttons or textfields to side panel here...
		
		JComponent topPanel=new JPanel();
		topPanel.setLayout(new FlowLayout());
		topPanel.add(new JLabel("Block Demolition Game"));
		mainPanel.add(topPanel, BorderLayout.NORTH);
		
		JEasyFrame frame = new JEasyFrame(mainPanel, "Block Demolition Game");
		view.addKeyListener(new BasicKeyListener());
		view.addMouseMotionListener(new BasicMouseListener());

                // recreate all particles in their original positions:
                final BasicPhysicsEngineUsingBox2D game2 = new BasicPhysicsEngineUsingBox2D ();
                try {
                startThread(game2, view); // start a new thread for the new game object:
                } catch (InterruptedException e1) {
                        e1.printStackTrace();
                }
                // Tell the view object to start displaying this new Physics engine instead:
                view.updateGame(game2);
                view.requestFocus();// needed for keyboard listener to work - it would be
                // better off to rewrite using Swing's "Key Bindings" apparently as this
                // will remove the need for focus.
                //
                
		ActionListener listener=new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==jButton_go) {
                                    float c = (float) Math.cos(Float.parseFloat(angle.getText()) /180 * Math.PI);
                                    float s = (float) Math.sin(Float.parseFloat(angle.getText()) /180 * Math.PI);
                                    float sp = Float.parseFloat(speed.getText());
                                    game2.particles.add(new BasicParticle(0,WORLD_HEIGHT / 4,c * sp, s * sp, .3f ,Color.GREEN, 1, 1));

				
				}
			}
		};
		jButton_go.addActionListener(listener);
	}
	private static void startThread(final BasicPhysicsEngineUsingBox2D game, final BasicView view) throws InterruptedException {
	    Runnable r = new Runnable() {
	         public void run() {
	        	// this while loop will exit any time this method is called for a second time, because 
	    		while (theThread==Thread.currentThread()) {
    				game.update();
    				view.repaint();
	    			try {
						Thread.sleep(BasicPhysicsEngineUsingBox2D.DELAY);
					} catch (InterruptedException e) {
					}
	    		}
	         }
	     };

	     theThread=new Thread(r);// this will cause any old threads running to self-terminate
	     theThread.start();
	}
	


	
	

}


