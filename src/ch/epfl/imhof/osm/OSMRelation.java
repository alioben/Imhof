package ch.epfl.imhof.osm;

/**
 * Représente une relation OSM
 *
 * @author Ben Lalah Ali
 * @author Alami Idrissi Ali
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

public final class OSMRelation extends OSMEntity {

    private final List<Member> members;

    /**
     * Construit une relation étant donnés son identifiant unique, ses membres
     * et ses attributs.
     * 
     * @param id
     *            : Identifiant de la relation OSM.
     * @param members
     *            : Liste des Membres de la relation OSM.
     * @param attributes
     *            : Attributs de la relation OSM.
     */
    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        this.members = Collections.unmodifiableList(new ArrayList<Member>(
                members));
    }

    /**
     * 
     * @return La liste des membres de la relation.
     */
    public List<Member> members() {
        return this.members;
    }

    /**
     * Représente un membre d'une relation OSM.
     *
     * @author Ben Lalah Ali (251758)
     * @author Alami Idrissi Ali (251759)
     */
    public final static class Member {
        private final OSMEntity member;
        private final Type type;
        private final String role;

        public enum Type {
            NODE, WAY, RELATION
        };

        /**
         * Construit un membre ayant le type, le rôle et la valeur donnés.
         * 
         * @param type
         *            : Type du membre.
         * @param role
         *            : Role du membre.
         * @param member
         *            :
         */
        public Member(Type type, String role, OSMEntity member) {
            this.role = role;
            this.member = member;
            this.type = type;
        }

        /**
         * 
         * @return Le type du membre.
         */
        public Type type() {
            return this.type;
        }

        /**
         * 
         * @return Le role du membre.
         */
        public String role() {
            return this.role;
        }

        /**
         * 
         * @return Le membre lui-meme.
         */
        public OSMEntity member() {
            return this.member;
        }

    }

    /**
     * Construit un batisseu d'une relation OSM.
     *
     * @author Ben Lalah Ali (251758)
     * @author Alami Idrissi Ali (251759)
     */
    public static final class Builder extends OSMEntity.Builder {
        private List<Member> members;

        /**
         * Construit un bâtisseur pour une relation ayant l'identifiant donné.
         * 
         * @param id
         *            : Identifiant de la relation.
         */
        public Builder(long id) {
            super(id);
            this.members = new ArrayList<>();
        }

        /**
         * Ajoute un nouveau membre de type et de rôle donnés à la relation.
         * 
         * @param type
         *            : Type du nouveau membre.
         * @param role
         *            : Role du nouveau membre.
         * @param newMember
         *            :
         */
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            members.add(new Member(type, role, newMember));
        }

        /**
         * Construit et retourne la relation ayant l'identifiant passé au
         * constructeur ainsi que les membres et les attributs ajoutés jusqu'à
         * présent au bâtisseur.
         * 
         * @throws IllegalStateException
         *             : Lève l'exception si la relation en cours de
         *             construction est incomplète.
         * @return La relation en construction.
         */
        public OSMRelation build() {
            if (super.isIncomplete())
                throw new IllegalStateException();
            else
                return new OSMRelation(super.id(), members,
                        super.getAttributes());
        }
    }
}
