/*
 * Copyright (c) 2013 DesktopScrollPane Pedro Duque Vieira. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of DesktopScrollPane Pedro Duque Vieira nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tn.manianis.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

public class DesktopScrollPane extends JDesktopPane {

    private InternalFrameComponentListener componentListener;

    public DesktopScrollPane() {
        componentListener = new InternalFrameComponentListener();
        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                onComponentAdded(e);
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                onComponentRemoved(e);
            }
        });

    }

    private void onComponentRemoved(ContainerEvent event) {
        Component removedComponent = event.getChild();
        if (removedComponent instanceof JInternalFrame) {
            removedComponent.removeComponentListener(componentListener);
            resizeDesktop();
        }

    }

    private void onComponentAdded(ContainerEvent event) {
        Component addedComponent = event.getChild();
        if (addedComponent instanceof JInternalFrame) {
            addedComponent.addComponentListener(componentListener);
            resizeDesktop();
        }
    }

    /**
     * resizes the desktop based upon the locations of its internal frames. This
     * updates the desktop scrollbars in real-time.
     */
    public void resizeDesktop() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                // has to go through all the internal frames now and make sure none
                // off screen, and if so, add those scroll bars!
                Rectangle viewPort = getBounds();

                int maxX = viewPort.x + viewPort.width, maxY = viewPort.y + viewPort.height;
                int minX = viewPort.x, minY = viewPort.y;

                // determine the min/max extents of all internal frames
                JInternalFrame frame = null;
                JInternalFrame[] frames = getAllFrames();

                for (int i = 0; i < frames.length; i++) {

                    frame = frames[i];

                    if (frame.getX() < minX) { // get minimum X
                        minX = frame.getX();
                    }
                    if ((frame.getX() + frame.getWidth()) > maxX) {
                        maxX = frame.getX() + frame.getWidth();
                    }

                    if (frame.getY() < minY) { // get minimum Y
                        minY = frame.getY();
                    }
                    if ((frame.getY() + frame.getHeight()) > maxY) {
                        maxY = frame.getY() + frame.getHeight();
                    }
                }

                // Don't count with frames that get off screen from the left side and the top
//                if (minX < 0) {
//                    minX = 0;
//                }
//                if (minY < 0) {
//                    minY = 0;
//                }
                setVisible(false); // don't update the viewport
                // while we move everything (otherwise desktop looks 'bouncy')

//                if (minX != 0 || minY != 0) {
//                    // have to scroll it to the right or up the amount that it's off screen...
//                    // before scroll, move every component to the right / down by that amount
//
//                    for (int i = 0; i < frames.length; i++) {
//                        frame = frames[i];
//                        frame.setLocation(frame.getX() - minX, frame.getY() - minY);
//                    }
//                }
                setLocation(minX, minY);
                setPreferredSize(new Dimension(maxX - minX, maxY - minY));
                setVisible(true); // update the viewport again
            }
        });
    }

    private class InternalFrameComponentListener implements ComponentListener {

        @Override
        public void componentResized(ComponentEvent e) {
            resizeDesktop();
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            resizeDesktop();
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }
    }
}
