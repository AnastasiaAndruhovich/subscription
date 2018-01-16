package by.andruhovich.subscription.entity;

import by.andruhovich.subscription.dao.*;
import by.andruhovich.subscription.dao.impl.AuthorPublicationDAO;
import by.andruhovich.subscription.dao.impl.GenreDAO;
import by.andruhovich.subscription.dao.impl.PublicationDAO;
import by.andruhovich.subscription.dao.impl.PublicationTypeDAO;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Publication {
    private int publicationId;
    private String name;
    private String description;
    private BigDecimal price;
    private String pictureName;
    private byte[] picture;

    private List<Author> authors;
    private Genre genre;
    private PublicationType publicationType;

    public Publication(String name, String description, BigDecimal price, Author author, Genre genre, PublicationType publicationType) {
        this.name = name;
        this.description = description;
        this.price = price;
        authors = new LinkedList<>();
        authors.add(author);
        this.genre = genre;
        this.publicationType = publicationType;
    }

    public Publication(String name, String description, BigDecimal price, String pictureName, byte[] picture) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.pictureName = pictureName;
        this.picture = picture;
    }

    public Publication(String name, String description, BigDecimal price, List<Author> authors, Genre genre, PublicationType publicationType) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.authors = authors;
        this.genre = genre;
        this.publicationType = publicationType;
    }

    public Publication(int publicationId, String name, String description, BigDecimal price, String pictureName, byte[] picture) {
        this.publicationId = publicationId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.pictureName = pictureName;
        this.picture = picture;
    }

    public Publication(int publicationId, String name, String description, BigDecimal price, List<Author> authors, Genre genre, PublicationType publicationType) {
        this.publicationId = publicationId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.authors = authors;
        this.genre = genre;
        this.publicationType = publicationType;
    }

    public int getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public List<Author> getAuthors() throws ConnectionTechnicalException, DAOTechnicalException {
        if (authors == null) {
            DAOFactory daoFactory = DAOFactory.getInstance();
            AuthorPublicationDAO authorPublicationDAO = daoFactory.createAuthorPublicationDAO();
            authors = authorPublicationDAO.findAuthorByPublicationId(publicationId);
        }
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Genre getGenre() throws ConnectionTechnicalException, DAOTechnicalException {
        if (genre == null) {
                DAOFactory daoFactory = DAOFactory.getInstance();
                PublicationDAO publicationDAO = daoFactory.createPublicationDAO();
                int genreId = publicationDAO.findGenreIdByPublicationId(publicationId);
                if (genreId != 0) {
                    GenreDAO genreDAO = daoFactory.createGenreDAO();
                    genre = genreDAO.findEntityById(genreId);
                }
                //???
                else return null;
        }
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public PublicationType getPublicationType() throws ConnectionTechnicalException, DAOTechnicalException {
        if (publicationType == null) {
            DAOFactory daoFactory = DAOFactory.getInstance();
            PublicationDAO publicationDAO = daoFactory.createPublicationDAO();
            int publicationTypeId = publicationDAO.findGenreIdByPublicationId(publicationId);
            if (publicationTypeId != 0) {
                PublicationTypeDAO publicationTypeDAO = daoFactory.createPublicationTypeDAO();
                publicationType = publicationTypeDAO.findEntityById(publicationTypeId);
            }
        }
        return publicationType;
    }

    public void setPublicationType(PublicationType publicationType) {
        this.publicationType = publicationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publication)) return false;
        Publication that = (Publication) o;
        return publicationId == that.publicationId &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(pictureName, that.pictureName) &&
                Arrays.equals(picture, that.picture) &&
                Objects.equals(authors, that.authors) &&
                Objects.equals(genre, that.genre) &&
                Objects.equals(publicationType, that.publicationType);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(publicationId, name, description, price, pictureName, authors, genre, publicationType);
        result = 31 * result + Arrays.hashCode(picture);
        return result;
    }
}
