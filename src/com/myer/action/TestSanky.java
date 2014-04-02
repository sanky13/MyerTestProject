package com.myer.action;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class TestSanky {


    private static DustEngine2 engine;

	
	public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
		DustOptions options = new DustOptions();
		engine = new DustEngine2(options);
        String expected = "(function(){dust.register(\"test\",body_0);function body_0(chk,ctx){return chk.write(" +
        "\"Hello \").reference(ctx.get(\"name\"),ctx,\"h\").write(\"! You have \").reference(ctx.get(\"count\")," +
        "ctx,\"h\").write(\" new messagesXXXXXXXX.\");}return body_0;})();";
//   System.out.println( engine.compile("Hello {name}! You have {count} new messages.", "test"));
   
   engine.load("Hello {name}! You have {count} new messages.", "test");

   engine.render("test","json" );

	}

}