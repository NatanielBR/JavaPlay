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
package javaplay.thread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javaplay.JavaPlay;
import javaplay.outros.InserirResultado;
import javaplay.outros.ObterResultado;
import javaplay.outros.Resultado;
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
		} catch (SQLException e) {
			JavaPlay.error(e);
		}
	}
	/**
	 * Metodo para inserir o tempo e o arquivo  no banco de dados
	 * @param inres
	 */
	public void inserirResultado(InserirResultado inres) {
		PreparedStatement pre;
		try {
			pre = con.prepareStatement(
					"INSERT INTO Arquivos (Nome,Caminho,Assistido)VALUES (?,?,?) on conflict(Caminho) do update set Assistido=?;");
			pre.setString(1, inres.getArquivo().getName());
			pre.setString(2, inres.getArquivo().getAbsolutePath());
			pre.setInt(3, inres.getUltimoTempo());
			pre.setInt(4, inres.getUltimoTempo());
			pre.execute();
		} catch (SQLException e) {
			JavaPlay.error(e);
		}

	}
	/**
	 * Metodo para obter o resultado, tempo e arquivo do banco de dados
	 * @param obter
	 * @return
	 */
	public Resultado obterResultado(ObterResultado obter) {
		
		try {
			ResultSet re = con.createStatement()
			.executeQuery("select * from Arquivos where Nome == '" + obter.getArq().getName() + "'");
			Resultado resultado = null;
			while (re.next()) {
				int a = re.getInt(3);
				resultado = new Resultado(a, obter.getArq());
			}
			return resultado;
		} catch (SQLException e) {
			JavaPlay.error(e);
		}
		
		return null;
	}

}
