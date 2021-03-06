/*******************************************************************************
 * Copyright (C) 2019 natan
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaplay.outros;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Classe para obter as propriedades
 * @author Nataniel
 */
public class Propriedades {

    public static Propriedades instancia;
    private final String arquivo = "JavaPlay.properties";
    
    public Propriedades() {
        instancia = this;
    }
    /**
     * Metodo para obter o diretorio
     * @return
     */
    public String getDir() {
    	return carregar().getProperty("dir",null);
    }
    /**
     * Metodo para alterar o diretorio
     * @param nv
     */
    public void setDir(String nv) {
        Properties prop = carregar();
        prop.put("dir", nv);
        try {
            prop.store(saida(), "");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void removerDir() {
    	Properties prop = carregar();
    	prop.remove("dir");
    	try {
			prop.store(saida(), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public boolean isPularAbertura() {
    	return Boolean.parseBoolean(carregar().getProperty("pularAbertura", "false"));
    }
    public void setPularAbertura(boolean valor) {
    	Properties prop =  carregar();
    	prop.put("pularAbertura", Boolean.toString(valor));
    	try {
			prop.store(saida(), "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void setProximoArquivo(boolean valor) {
    	Properties prop =  carregar();
    	prop.put("proximoArquivo", Boolean.toString(valor));
    	try {
			prop.store(saida(), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public boolean isProximoArquivo() {
    	return Boolean.parseBoolean(carregar().getProperty("proximoArquivo", "false"));
    }
    
    private Properties carregar() {
        Properties prop = new Properties();
        File f = new File(arquivo);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            prop.load(new FileInputStream(f));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prop;
    }

    private OutputStream saida() {
        OutputStream out = null;
        try {
            out = new FileOutputStream(arquivo);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return out;
    }
}
