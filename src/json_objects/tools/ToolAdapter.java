package json_objects.tools;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
//! Credit for this code goes to http://ovaraksin.blogspot.com/2011/05/json-with-gson-and-abstract-classes.html
//! \details Allows Gson to use the correct child class of Tool
public class ToolAdapter implements JsonSerializer<Tool>, JsonDeserializer<Tool> {

	@Override
	public Tool deserialize(JsonElement element, Type typeof,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = element.getAsJsonObject();
		String type = object.get("type").getAsString();
		JsonElement result = object.get("properties");
		try {
			return context.deserialize(result, Class.forName(type));
		}
		catch (ClassNotFoundException excep)
		{
			throw new JsonParseException("Unknown element type: json_objects.tools."+ type,excep);
		}
	}
	@Override
	public JsonElement serialize(Tool src, Type typeofsrc,
			JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
		result.add("properties", context.serialize(src,src.getClass()));
		return result;
	}

}
