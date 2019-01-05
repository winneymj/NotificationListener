/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package learn2crack.notificationlistener.persistence.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import learn2crack.notificationlistener.persistence.db.model.NotificationApps;

@Entity(tableName = "notificationApps")
public class NotificationAppsEntity implements NotificationApps {
    @PrimaryKey
    private int id;
    private String appName;
    private String packageName;
    private Boolean enabled;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getAppName() {
        return appName;
    }

    public void setAppName(String name) {
        this.appName = name;
    }

    @Override
    public String getPackageName() { return packageName; }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public NotificationAppsEntity() {
    }

    @Ignore
    public NotificationAppsEntity(int id, String name, String packageName, Boolean enabled) {
        this.id = id;
        this.appName = name;
        this.packageName = packageName;
        this.enabled = enabled;
    }

    public NotificationAppsEntity(NotificationApps apps) {
        this.id = apps.getId();
        this.appName = apps.getAppName();
        this.packageName = apps.getPackageName();
        this.enabled = apps.getEnabled();
    }
}