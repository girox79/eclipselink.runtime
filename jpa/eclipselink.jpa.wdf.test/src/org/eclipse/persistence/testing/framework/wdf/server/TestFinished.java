/*******************************************************************************
 * Copyright (c) 2005, 2015 SAP. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     SAP - initial API and implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.framework.wdf.server;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

/**
 * A notification indicating that the execution of a test has finished.
 */
public class TestFinished implements Notification {

    private static final long serialVersionUID = 1L;
    private final SerializableDescription description;

    public TestFinished(Description aDescription) {
        description = SerializableDescription.create(aDescription);
    }

    @Override
    public void notify(RunNotifier notifier) {
        notifier.fireTestFinished(description.restore());
    }

}
