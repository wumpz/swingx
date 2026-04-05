/*
 * Created on 02.02.2012
 *
 */
package org.jdesktop.swingx;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.test.XTestUtils;
import org.jdesktop.swingx.util.GraphicsUtilities;
import org.jdesktop.test.EDTRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Unit and visual tests for JXImageView
 * @author Jeanette Winzenburg, Berlin
 */
@ExtendWith(EDTRunner.class)
public class JXImageViewTest extends InteractiveTestCase {

    /**
     * Issue java.net/jira/browse/SWINGX-1479: 
     *     ImageView should use backgroundPainter 
     */
    @Test
    public void testBackgroundPainterDefault() {
        JXImageView panel = new JXImageView();
        Painter<?> background = panel.getBackgroundPainter();
        assertTrue(background instanceof MattePainter, "default background painter should be MattePainter "
                + " but was: " + background);
    }
    
//------------------ interactive
    
    public static void main(String[] args) {
        JXImageViewTest test = new JXImageViewTest();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Issue java.net/jira/browse/SWINGX-1479: 
     *     ImageView should use backgroundPainter 
     */
    public void interactiveBackgroundPainterUsed() {
        BufferedImage im = XTestUtils.loadDefaultImage(
                "500by500.png");
        BufferedImage im2 = GraphicsUtilities.createThumbnail(im, 100);
        JXImageView ipa = new JXImageView();
        ipa.setBackgroundPainter(new MattePainter(Color.RED));
        ipa.setImage(im2);
        showInFrame(ipa, "Imageview background");
        
    }
}
