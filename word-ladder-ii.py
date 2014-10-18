class Solution:
    # @param start, a string
    # @param end, a string
    # @param dict, a set of string
    # @return a list of lists of string
    def findLadders(self, start, end, dict):
        
        if len(start) != len(end):
            raise Exception('the length of start=={d} and end=={d} does not match'.format(len(start), len(end)))
            
        if len(start) == 0:
            return []
        elif len(start) == 1:
            return [[start, end]]
        elif start == end:
            return [[start]]
    
        connected_words = self.find_connecting_words(start, end, dict)
        
        shortest_path = self.find_shortest_path(start, end, connected_words)
        
        return self.find_word_ladders(start, end, shortest_path)
        
    # @param start, a string
    # @param end, a string
    # @param dict, a list of string        
    def find_connecting_words(self, start, end, dict):
        ''' Find all the words that can be transformed to each other by changing one letter
        '''
        words = set(dict)
        words.add(start)
        words.add(end)
        
        connected_words = {}
        for word in words:
            connected_words[word] = []
        
        for word in words:
            for i in range(len(word)):
                letter = word[i]
                
                for replacement_letter in string.ascii_lowercase:
                    if replacement_letter != letter:
                        word_with_letter_replaced = word[:i] + replacement_letter + word[i+1:]
                        if word_with_letter_replaced in words:
                            connected_words[word].append(word_with_letter_replaced)
                        
        return connected_words

    # @param start, a string
    # @param end, a string
    # @param connected_words, a dict of string
    def find_shortest_path(self, start, end, connected_words):
        ''' Using BSF traverse to find shortest path between src node and dst node with unit weight edges
        '''
        shortest_path = {}
        distance_table = {}
        for word in connected_words.keys():
            shortest_path[word] = []
            distance_table[word] = float('inf')
            
        distance_table[end] = 0
        
        layer_of_words = set()
        layer_of_words.add(end)
        next_layer_of_words = set()
    
        is_path_found = False
        while len(layer_of_words) > 0:
            word = layer_of_words.pop()
            
            for neighbor_word in connected_words[word]:
                if distance_table[neighbor_word] >= distance_table[word] + 1:
                    distance_table[neighbor_word] = distance_table[word] + 1
                    shortest_path[neighbor_word].append(word)
                    next_layer_of_words.add(neighbor_word)
                        
            if len(layer_of_words) == 0:
                if start in next_layer_of_words:
                    is_path_found = True
                    break
                else:
                    (layer_of_words, next_layer_of_words) = (next_layer_of_words, layer_of_words)
        
        if is_path_found is True:
            return shortest_path
        else:
            return []

    # @param start, a string
    # @param end, a string
    # @param shortest_path, a map of string to a list of strings
    def find_word_ladders(self, start, end, shortest_path):
        ''' Given the shortest transformation of words, recursively find the word ladders that connect the start and end word
        '''
        if len(shortest_path) == 0:
            return []
            
        word_ladders = []    
        self.find_ladder_passing_by_this_word(start, end, [start], shortest_path, word_ladders)
        
        return word_ladders
 
    # @param start, a string
    # @param end, a string
    # @param ladder_passing_by_this_word, a list of string
    # @param word_ladders, a list of lists of strings      
    def find_ladder_passing_by_this_word(self, word, end, ladder_passing_by_this_word, shortest_path, word_ladders):
        ''' Given the shortest transformation of words, recursively find the word ladders that pass by a specific node
        '''        
        if word == end:
            word_ladders.append(ladder_passing_by_this_word)
            return
            
        for next_word in shortest_path[word]:
            if next_word is not None:
                ladder_passing_by_next_word = copy.deepcopy(ladder_passing_by_this_word)
                ladder_passing_by_next_word.append(next_word)
                self.find_ladder_passing_by_this_word(next_word, end, ladder_passing_by_next_word, shortest_path, word_ladders)
