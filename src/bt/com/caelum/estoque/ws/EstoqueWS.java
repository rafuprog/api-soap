package bt.com.caelum.estoque.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import br.com.caelum.estoque.modelo.item.Filtro;
import br.com.caelum.estoque.modelo.item.Filtros;
import br.com.caelum.estoque.modelo.item.Item;
import br.com.caelum.estoque.modelo.item.ItemDao;
import br.com.caelum.estoque.modelo.item.ItemValidador;
import br.com.caelum.estoque.modelo.item.ListaItens;
import br.com.caelum.estoque.modelo.usuario.AuterizacacaoException;
import br.com.caelum.estoque.modelo.usuario.TokenDao;
import br.com.caelum.estoque.modelo.usuario.TokenUsuario;

@WebService
public class EstoqueWS {
	private ItemDao dao = new ItemDao();

	@WebMethod(operationName="todosOsItens") 
    @WebResult(name="itens")
    public ListaItens getItens(@WebParam(name="filtros") Filtros filtros) {
        System.out.println("Chamando getItens()");
        List<Filtro> lista = filtros.getLista();
        List<Item> itensResultado = dao.todosItens(lista);
        return new ListaItens(itensResultado);
    }

	@WebMethod(operationName = "CadastrarItem")
	public Item cadastrarItem(@WebParam(name = "tokenUsuario", header = true) TokenUsuario token,
			@WebParam(name = "item") Item item) throws AuterizacacaoException {

		System.out.println("Cadastrando " + item + ", " + token);

		if (!new TokenDao().ehValido(token)) {
			throw new AuterizacacaoException("Autorizacao falhou");
		}

		new ItemValidador(item).validate();

		this.dao.cadastrar(item);
		return item;
	}
}
