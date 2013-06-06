package json_objects;

import java.util.zip.DataFormatException;

import json_objects.tools.Tool;
//! \details used to receive commands
public class Message
{
	public enum COMMAND{FILE_REQUEST,FILE_COMPILE,UPLOAD,FOLDER,STATUS,ACTION}
	private String action;
	private FileInput[] files;
	private Tool[] filters;
	public void setTools(Tool[] value)
	{
		filters = value;
	}
	public void setType(String value)
	{
		action = value;
	}
	public Tool[] getTools()
	{
		return filters;
	}
	/*!
	 * Function: getCommand
	 * \return COMMAND
	 * \details return what operation is being request, it throws and error if it doesn't recognize the request operation
	 */
	public COMMAND getCommand() throws DataFormatException
	{
		if(action.equals("file_request"))
			return COMMAND.FILE_REQUEST;
		else if(action.equals("file_compile"))
			return COMMAND.FILE_COMPILE;
		else if(action.equals("upload"))
			return COMMAND.UPLOAD;
		else if(action.equals("folder"))
			return COMMAND.FOLDER;
		else if(action.equals("status"))
			return COMMAND.STATUS;
		else if(action.equals("action"))
			return COMMAND.ACTION;
		else
			throw new DataFormatException("invalid message type: expected file, status, or action");
	}
	public FileInput[] getFiles() {
		return files;
	}
	public void setFiles(FileInput[] files) {
		this.files = files;
	}
}
