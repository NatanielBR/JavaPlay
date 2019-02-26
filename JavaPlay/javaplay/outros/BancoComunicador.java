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
package javaplay.outros;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Classe para se comunicar com o banco de dados.
 * @author natan
 *
 */
public class BancoComunicador{
	public static BancoComunicador instancia;
	private Connection con;
	
	public BancoComunicador() {
		instancia = this;
		try {
			con = DriverManager.getConnection("jdbc:sqlite:Database.db3");
			con.createStatement().execute("CREATE TABLE IF NOT EXISTS Arquivos (\n" + "    Nome      TEXT,\n"
					+ "    Caminho   TEXT    UNIQUE,\n" + "    Assistido INTEGER\n" + ");");
			con.createStatement().execute("CREATE TABLE IF NOT EXISTS Regexs (\n" + 
					"	Regex	TEXT,\n" + 
					"	Arquivos	TEXT\n" + 
					")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Metodo para inserir o tempo e o arquivo  no banco de dados
	 * @param inres
	 */
	public void inserirResultado(int ultimoTempo, File arquivo) {
		PreparedStatement pre;
		try {
			pre = con.prepareStatement(
					"INSERT INTO Arquivos (Nome,Caminho,Assistido)VALUES (?,?,?) on conflict(Caminho) do update set Assistido=?;");
			pre.setString(1, arquivo.getName());
			pre.setString(2, arquivo.getAbsolutePath());
			pre.setInt(3, ultimoTempo);
			pre.setInt(4, ultimoTempo);
			pre.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Metodo para obter o resultado, tempo e arquivo do banco de dados
	 * @param obter
	 * @return
	 */
	public Resultado obterResultado(File arquivo) {
		
		try {
			ResultSet re = con.createStatement()
			.executeQuery("select * from Arquivos where Nome == '" + arquivo.getName() + "'");
			Resultado resultado = null;
			while (re.next()) {
				int a = re.getInt(3);
				resultado = new Resultado(a, arquivo);
			}
			return resultado;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * Metodo para inserir a regex  no banco de dados
	 * @param inres
	 */
	public void inserirRegex(String regex, File ... arquivo) {
		PreparedStatement pre;
		try {
			pre = con.prepareStatement(
					"INSERT INTO Regexs (Regex,Arquivos)VALUES (?,?) on conflict(Arquivos) do update set Arquivos=?;");
			pre.setString(1, regex);
			StringBuilder bu = new StringBuilder();
			for (int index = 0; index < arquivo.length; index++) {
				bu.append(arquivo[index].getAbsolutePath()).append("|");
			}
			pre.setString(2, bu.toString());
			pre.setString(3, bu.toString());
			pre.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Metodo para obter o resultado, tempo e arquivo do banco de dados
	 * @param obter
	 * @return
	 */
	public Regex[] obterRegexs(File arquivo) {
		List<Regex> lista = new ArrayList<>();
		try {
			ResultSet re = con.createStatement()
			.executeQuery("select * from Regexs");
			while (re.next()) {
				String reg = re.getString(1);
				String arquivos = re.getString(2);
				lista.add(new Regex(reg, arquivos.split("|")));
			}
			return lista.toArray(new Regex[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
