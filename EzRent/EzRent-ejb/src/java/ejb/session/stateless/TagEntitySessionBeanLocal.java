/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TagEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewTagException;
import util.exception.DeleteTagException;
import util.exception.TagNotFoundException;
import util.exception.UpdateTagFailException;

/**
 *
 * @author kiyon
 */
@Local
public interface TagEntitySessionBeanLocal {

    public TagEntity createNewTag(TagEntity tag) throws CreateNewTagException;

    public List<TagEntity> retrieveAllTags();

    public TagEntity retrieveTagByTagId(Long tagId) throws TagNotFoundException;

    public TagEntity updateTagName(Long tagId, String newName) throws TagNotFoundException, UpdateTagFailException;

    public void deleteTag(Long tagId) throws TagNotFoundException, DeleteTagException;

    public TagEntity retrieveTagByTagName(String tagName) throws TagNotFoundException;


}
