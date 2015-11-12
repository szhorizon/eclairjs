/*
 * Copyright 2015 IBM Corp.
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

package org.eclairjs.nashorn;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.apache.commons.lang.ArrayUtils;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.ArrayList;

/**
 * Created by bburns on 9/13/15.
 */
public class JSPairFunction implements PairFunction {


    private final String func;
    private final Object[] scopeVar;
    private String functionName = null;

    public JSPairFunction(String func, Object[] o) {
    	this.functionName = Utils.getUniqeFunctionName();
        this.func = "var " + this.functionName +" = " + func;
	    this.scopeVar = o;
    }

    @Override
    public Tuple2 call(Object a) throws Exception {

        ScriptEngine e = NashornEngineSingleton.getEngine(); 
  
        e.eval(this.func);
        Invocable invocable = (Invocable) e;
        Object o = Utils.javaToJs(a, e);
        Object params[] = {o};
       
        params = ArrayUtils.addAll(params, this.scopeVar);
        ScriptObjectMirror ret = (ScriptObjectMirror) invocable.invokeFunction(this.functionName, params); // FIXME reusing the function name in same engine not a good idea

        ArrayList l = new ArrayList(ret.values());
        Object t1 = Utils.jsToJava(l.get(0));
        Object t2 = Utils.jsToJava(l.get(1));
        Tuple2 t = new Tuple2(t1, t2);

        return t;
    }
}