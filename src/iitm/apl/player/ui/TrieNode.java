package iitm.apl.player.ui;

import iitm.apl.player.Song;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

@SuppressWarnings("unchecked")
public class TrieNode implements Comparable {
	// to denote whether any word /string ends at this node
	private boolean end = false;
	// array of array lists storing the nodes(chain hashing)
	private ArrayList<TrieNode> children;
	// to denote the letter
	private char letter = 0;
	private ArrayList<Song> song = new ArrayList();

	public void addSong(Song song) {
		this.song.add(song);

	}

	public TrieNode() {
		children = new ArrayList();
	}

	public void add(Vector<Song> a) {
		for (int i = 0; i < a.size(); i++) {
			String[] str = new String[100];
			if (a.get(i) != null && a.get(i).getTitle() != null) {
				str = a.get(i).getTitle().toLowerCase().split(" ");
				this.add(a.get(i).getTitle().toLowerCase(), a.get(i));
				for (int j = 0; j < str.length; j++) {
					this.add(str[j], a.get(i));
				}
			}
			if (a.get(i) != null && a.get(i).getArtist() != null) {
				str = a.get(i).getArtist().toLowerCase().split(" ");
				this.add(a.get(i).getArtist().toLowerCase(), a.get(i));
				for (int j = 0; j < str.length; j++) {
					this.add(str[j], a.get(i));
				}
			}
			if (a.get(i) != null && a.get(i).getAlbum() != null) {
				str = a.get(i).getAlbum().toLowerCase().split(" ");
				this.add(a.get(i).getAlbum().toLowerCase(), a.get(i));
				for (int j = 0; j < str.length; j++) {
					this.add(str[j], a.get(i));
				}
			}
			// this.add(a.get(i).getAlbum().toLowerCase(), a.get(i));
			// this.add(a.get(i).getArtist().toLowerCase(), a.get(i));
		}
	}

	// constructor with the specific letter
	public TrieNode(char x) {
		this.letter = x;
	}

	public void add(String word, Song song) {
		if (word.length() == 0)
			return;
		char current = word.charAt(0);
		if (bin_search(current, this.children, 0, this.children.size() - 1) == null) {
			TrieNode trie_node = new TrieNode();
			trie_node.letter = current;
			children.add(trie_node);
			Collections.sort(children);
		}
		TrieNode temp = bin_search(current, this.children, 0, this.children
				.size() - 1);
		if (word.length() > 1) {
			temp.add(word.substring(1), song);
		} else {
			if (temp.end == false) {
				temp.end = true;
				// temp.song= new Song[100];
			}
			temp.addSong(song);
		}
	}

	public TrieNode bin_search(char c, ArrayList<TrieNode> t, int p, int q) {
		if (p > q) {
			return null;
		}
		int n = (q + p + 1) / 2;
		char curr = t.get(n).letter;
		if (p == q) {
			if (t.get(q).letter == c) {
				return t.get(q);
			} else {
				return null;
			}
		}
		if (q - p <= 2) {
			for (int i = p; i < q + 1; i++) {
				if (t.get(i).letter == c)
					return t.get(i);
			}
			return null;
		} else if (curr == c) {
			return t.get(n);
		} else if (curr < c) {
			return bin_search(c, t, n + 1, q);

		} else {
			return bin_search(c, t, 0, n - 1);
		}
	}

	protected TrieNode exists(String word) {
		if (word.length() == 0)
			return null;
		char current = word.charAt(0);
		TrieNode temp = bin_search(current, this.children, 0, this.children
				.size() - 1);
		if (temp != null && word.length() > 1) {
			return temp.exists(word.substring(1));
		} else if (temp != null && temp.end == true && word.length() == 1) {
			return temp;
		} else if (temp != null && word.length() == 1) {
			return temp;
		} else
			return null;

	}

	/*
	 * public boolean delete(String word) { char current = word.charAt(0);
	 * TrieNode temp = bin_search(current, this.children, 0, 1); if (temp !=
	 * null && word.length() > 1) { if (temp.delete(word.substring(1)) == true)
	 * { if (temp.children.size() == 0) return true; } else return false; } else
	 * if (temp != null && temp.end == true && word.length() == 1) { temp.end =
	 * false; temp.song = null; return true; } else return false; }
	 */

	private boolean isEmpty(TrieNode trieNode) {
		if (trieNode.children.size() == 0)
			return true;
		else
			return false;
	}

	public ArrayList<Song> retSongs(String word) {
		ArrayList<Song> songNames = new ArrayList();
		TrieNode temp = exists(word);
		if (temp == null)
			return null;
		if (temp.end == true) {
			for (int i = 0; i < temp.song.size(); i++) {
				songNames.add(temp.song.get(i));
			}
		}
		auto_complete_Songs(temp, songNames);
		return songNames;

	}

	public void auto_complete_Songs(TrieNode temp, ArrayList<Song> songNames) {
		if (temp.children == null) {
			return;
		}
		for (int i = 0; i < temp.children.size(); i++) {
			if (temp.children.get(i).end == true) {
				// if(!songNames.contains(temp.children.get(i).song))
				for (int j = 0; j < temp.children.get(i).song.size(); j++) {
					songNames.add(temp.children.get(i).song.get(j));
				}
			}
			auto_complete_Songs(temp.children.get(i), songNames);
		}
		return;
	}

	/*
	 * public void addAll( songs) { for (int i = 0; i < songs.length; i++) {
	 * String s = songs; add(s, ); } }
	 */

	@Override
	public int compareTo(Object arg0) {
		if (this.letter < ((TrieNode) arg0).letter)
			return 0;
		else
			return 1;
	}

}
