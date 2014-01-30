/*
    Copyright (C) 2012  Filippe Costa Spolti

	This file is part of Hrstatus.

    Hrstatus is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.hrstatus.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.hrstatus.utils.Images;
import br.com.hrstatus.utils.UserInfo;

/*
 * @author spolti
 */

@Resource
public class ImageController {
	
	private Result result;
	private Validator validator;
	private final Images images;
	private HttpServletRequest request;
	
	UserInfo userInfo = new UserInfo();
	
	public ImageController(Result result, Validator validator, Images images,HttpServletRequest request) {
		
		this.result = result;
		this.validator = validator;
		this.images = images;
		this.request = request;
	}
	
	@Post("/uploud/logo/imagem")
	 public void upload(final UploadedFile imagem){

		validator.onErrorRedirectTo(ConfigController.class).configServer();
		
		if (!imagem.getContentType().startsWith("image")){
				validator.add(new ValidationMessage("O arquivo enviado não é uma imagem.", "Erro"));
		} else{
			try{
				images.save(imagem);
				result.forwardTo(ConfigController.class).configServer();
			}catch(Exception e){
				validator.add(new ValidationMessage("Erro inesperado: " + e, "Erro"));
				
			}

		}
			
		validator.onErrorRedirectTo(ConfigController.class).configServer();
	}
	
	@Get("/delete/logo/imagem")
	public void delete(){
		
		images.delete();
		result.forwardTo(ConfigController.class).configServer();
		
	}
	
	@Get("/show/logo/imagem/{type}")
	public File download(String type) throws IOException {
		
	  
		if (type.equals("settings")){
			return images.show("settings");
		} else {
			return images.show("home");
		}
	}
	
	@Get("/show/emailHeader/{local}")
	public File emailHeader (String local) throws UnsupportedEncodingException{
		
		String UPfileName = request.getRealPath("img/up.jpg");
		String DOWNfileName = request.getRealPath("img/down.jpg");

		
		Logger.getLogger(getClass()).debug("FILE:  " + UPfileName);
		
		if (local.equals("up")){
			File file = new File(UPfileName);
			return file;
		} else if (local.equals("down")){
			File file = new File(DOWNfileName);
			return file;
		}
		
		return null;
	}
}