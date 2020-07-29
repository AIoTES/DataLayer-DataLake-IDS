/**
 * Copyright 2020 Universitat Politècnica de València
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dataLake;

import org.influxdb.annotation.Measurement;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

public class DynamicMeasurement implements Measurement{	
	private String name;
	private TimeUnit time;
	 
    public DynamicMeasurement(String name) {
        this.name = name;
        this.time = TimeUnit.MILLISECONDS;
    }
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public TimeUnit timeUnit() {
    	return time;
    }
 
    @Override
    public Class<? extends Annotation> annotationType() {
        return DynamicMeasurement.class;
    }
}
