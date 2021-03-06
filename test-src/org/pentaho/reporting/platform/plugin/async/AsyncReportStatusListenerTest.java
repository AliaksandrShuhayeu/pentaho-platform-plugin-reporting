/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2016 Pentaho Corporation..  All rights reserved.
 */

package org.pentaho.reporting.platform.plugin.async;

import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.event.ReportProgressEvent;
import org.pentaho.reporting.libraries.base.config.ModifiableConfiguration;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class AsyncReportStatusListenerTest {
  @Test
  public void isFirstPageMode() throws Exception {
    final ModifiableConfiguration edConf = ClassicEngineBoot.getInstance().getEditableConfig();
    edConf.setConfigProperty( "org.pentaho.reporting.platform.plugin.output.FirstPageMode", "true" );
    final AsyncReportStatusListener listener = new AsyncReportStatusListener( "", UUID.randomUUID(), "" );
    assertTrue( listener.isFirstPageMode() );
    edConf.setConfigProperty( "org.pentaho.reporting.platform.plugin.output.FirstPageMode", "false" );
    final AsyncReportStatusListener listener2 = new AsyncReportStatusListener( "", UUID.randomUUID(), "" );
    assertFalse( listener2.isFirstPageMode() );
  }

  @Test
  public void isScheduled() throws Exception {
    final AsyncReportStatusListener listener = new AsyncReportStatusListener( "", UUID.randomUUID(), "" );
    assertFalse( listener.isScheduled() );
    listener.setStatus( AsyncExecutionStatus.SCHEDULED );
    assertTrue( listener.isScheduled() );
  }

  @Test
  public void reportProcessingFinished() throws Exception {
    final AsyncReportStatusListener listener = new AsyncReportStatusListener( "", UUID.randomUUID(), "" );
    listener.reportProcessingFinished( mock( ReportProgressEvent.class ) );
    //listener should not update status to finished
    assertFalse( AsyncExecutionStatus.FINISHED.equals( listener.getState().getStatus() ) );
  }

  @Test
  public void setStatus() throws Exception {
    final AsyncReportStatusListener listener = new AsyncReportStatusListener( "", UUID.randomUUID(), "" );
    listener.setStatus( AsyncExecutionStatus.QUEUED );
    assertEquals( AsyncExecutionStatus.QUEUED, listener.getState().getStatus() );
    listener.setStatus( AsyncExecutionStatus.CANCELED );
    assertEquals( AsyncExecutionStatus.CANCELED, listener.getState().getStatus() );
    listener.setStatus( AsyncExecutionStatus.FINISHED );
    assertEquals( AsyncExecutionStatus.CANCELED, listener.getState().getStatus() );
  }

  @Test
  public void updateGenerationStatus() throws Exception {
    final AsyncReportStatusListener listener = new AsyncReportStatusListener( "", UUID.randomUUID(), "" );
    listener.setRequestedPage( 500 );
    listener.updateGenerationStatus( 500 );
    assertEquals( 0, listener.getRequestedPage() );
  }
}
