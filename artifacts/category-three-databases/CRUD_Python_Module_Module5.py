# Example Python Code to Insert a Document 

from pymongo import MongoClient 
from bson.objectid import ObjectId 
from urllib.parse import quote_plus

class AnimalShelter(object):
    """ CRUD operations for Animal collection in MongoDB """

    # Operators capable of executing arbitrary code or writing outside the
    # intended collection. Rejected from every query/pipeline this class runs
    # so filter criteria built from user-facing dashboard input can never
    # smuggle one in (NoSQL injection defense-in-depth).
    _DANGEROUS_OPERATORS = {'$where', '$function', '$accumulator', '$out', '$merge'}

    # Rescue-type filter profiles used by get_rescue_candidates(). Centralizing
    # these here (instead of duplicating the breed lists/age ranges in every
    # dashboard callback) means the dashboard only ever passes a rescue_type
    # string across the boundary, never a hand-built query.
    RESCUE_CRITERIA = {
        'water': {
            'breeds': ['Labrador Retriever Mix', 'Chesapeake Bay Retriever', 'Newfoundland Mix'],
            'sex': 'Intact Female',
            'min_age_weeks': 26,
            'max_age_weeks': 156,
        },
        'mountain': {
            'breeds': ['German Shepherd', 'Alaskan Malamute', 'Old English Sheepdog',
                       'Siberian Husky', 'Rottweiler'],
            'sex': 'Intact Male',
            'min_age_weeks': 26,
            'max_age_weeks': 156,
        },
        'track': {
            'breeds': ['Doberman Pinscher', 'German Shepherd', 'Golden Retriever',
                       'Bloodhound', 'Rottweiler'],
            'sex': 'Intact Male',
            'min_age_weeks': 20,
            'max_age_weeks': 300,
        },
    }

    def __init__(self,username,pwd):
        # Initializing the MongoClient. This helps to access the MongoDB 
        # databases and collections. This is hard-wired to use the aac 
        # database, the animals collection, and the aac user. 
        # 
        # You must edit the password below for your environment. 
        # 
        # Connection Variables 
        # 
        USER = username 
        PASS = pwd
        HOST = 'localhost' 
        PORT = 27017 
        DB = 'aac' 
        COL = 'animals' 
        # 
        # Initialize Connection 
        # 
        try:
            # Escape username and password to handle special characters
            escaped_username = quote_plus(USER)
            escaped_password = quote_plus(PASS)
            
            # Initialize connection with authentication
            self.client = MongoClient(f'mongodb://{escaped_username}:{escaped_password}@{HOST}:{PORT}')
            self.database = self.client[DB]
            self.collection = self.database[COL]
            print("Connection successful to MongoDB")
        except Exception as e:
            print(f"Error connecting to MongoDB: {e}")
            raise

    def _reject_dangerous_operators(self, obj):
        """
        Recursively walk a query dict or aggregation pipeline and raise if it
        contains an operator in _DANGEROUS_OPERATORS.
        """
        if isinstance(obj, dict):
            for key, value in obj.items():
                if key in self._DANGEROUS_OPERATORS:
                    raise ValueError(f"Disallowed operator in query: {key}")
                self._reject_dangerous_operators(value)
        elif isinstance(obj, list):
            for item in obj:
                self._reject_dangerous_operators(item)

    # Create a method to return the next available record number for use in the create method
            
    # Complete this create method to implement the C in CRUD. 
    def create(self, data):
        """
        Insert a document into the MongoDB Collection
        
        Parameters: 
        data (dict): A dictionary containing key/value pairs to insert
        
        Returns:
        
        bool: True if insert successful, False otherwise
        """
        try:
        
            
            if data is not None and isinstance(data,dict): 
                #insert the document into the collection
                result = self.database.animals.insert_one(data)  # data should be dictionary  
                #check if data has been inserted by the inserted_id
                if result.inserted_id:
                    return True
                else:
                    return False
                
            else: 
                #Data is invalid (None or not a dictionary)
                raise Exception("Error: Data parameter is empty or not a dictionary")
                return False
        except Exception as e:
            #Handle any exceptions during insertion
            print(f"Error occurred during create operation: {e}")
            return False

    # Create method to implement the R in CRUD.
    def read(self, query):
        """
        Query documents from the MongoDB Collection
        
        Parameters:
        
        query (dict): A dictionary containing key/value pairs for the search criteria
        
        Returns:
        
        list: A list of documents matching the query if found, otherwise empty list
        
        """
        try:
            
            #Validate the query is a dictionary
            
            if query is not None and isinstance(query, dict):
                #Reject operators that could execute code or write data before querying
                self._reject_dangerous_operators(query)
                #Execute the query and convert the cursor into a list
                cursor = self.collection.find(query)
                
                results = list(cursor)
                
                return results
            else:
                #query is invalid
                print("Error: Query parameter is empty or not a dictionary")
                return []
        except Exception as e:
            print(f"Error occurred during read operation: {e}")
            return []
        
    def read_all(self):
        """
        Retrieve all documents from the collection.
        
        Returns:
            list: A list of all documents in the collection
        """
        try:
            # Find all documents (empty query)
            cursor = self.collection.find({})
            results = list(cursor)
            return results
        except Exception as e:
            print(f"Error occurred during read_all operation: {e}")
            return []
        
    def update(self,query, update_fields):
        """
        Update records within a collection
        
        Parameters:
        
        query (dict): contains the query for which documents will be updated for
        update_fields (dict) : what columns should be changed to what corresponding value
        
        
        Returns:
        
        int: returns number of documents updated
        
        """
        
        
        
        #check whether there are existing documents for that query
        count = self.collection.count_documents(query)
        
        #if there no existing documents, then raise exception and return 0
        if(count == 0):
            raise Exception("Query resulted in no results. Please use another filter criteria")
            return 0
        try:
            #run the update command to update record(s) and return the number of modified documents
                update_results = self.collection.update_many(query, update_fields)
                return update_results.modified_count
        except Exception as e:
            print(f"An error occurred when updating document(s): {e}")
            
        
    def delete(self,query_filter):
        """
        Deletes documents within a collection based on query filter
        
        Parameters:
        
        query_filter(dict): constains filter criteria to delete document
        
        Returns:
        
        int: returns the number of documents deleted
        """
        #check whether there are documents that exist for that specific query
        count = self.collection.count_documents(query_filter)
        
        #if count is greater than 0, then execute the statement otherwise print
        if count > 0:
            results = self.collection.delete_many(query_filter)
            print(f"Deleted {results.deleted_count} documents")
            return results.deleted_count
        else:
            print("No documents to delete")
            return 0

    def aggregate(self, pipeline):
        """
        Run a server-side aggregation pipeline, so grouping/filtering happens
        in MongoDB instead of scanning a client-side copy of the collection.

        Parameters:
        pipeline (list): a list of aggregation stage dictionaries

        Returns:
        list: the aggregation results, or [] if the pipeline is invalid
        """
        try:
            if not isinstance(pipeline, list) or not all(isinstance(stage, dict) for stage in pipeline):
                raise ValueError("pipeline must be a list of stage dictionaries")
            self._reject_dangerous_operators(pipeline)
            return list(self.collection.aggregate(pipeline))
        except Exception as e:
            print(f"Error occurred during aggregate operation: {e}")
            return []

    def get_rescue_candidates(self, rescue_type):
        """
        Return dog records matching a named rescue-type profile (water,
        mountain, or track) via an index-backed aggregation $match, instead
        of pulling the full collection and filtering it with pandas.

        Parameters:
        rescue_type (str): one of the keys in RESCUE_CRITERIA

        Returns:
        list: matching animal documents
        """
        criteria = self.RESCUE_CRITERIA.get(rescue_type)
        if criteria is None:
            raise ValueError(f"Unknown rescue_type: {rescue_type}")
        pipeline = [
            {'$match': {
                'animal_type': 'Dog',
                'breed': {'$in': criteria['breeds']},
                'sex_upon_outcome': criteria['sex'],
                'age_upon_outcome_in_weeks': {
                    '$gte': criteria['min_age_weeks'],
                    '$lte': criteria['max_age_weeks']
                }
            }}
        ]
        return self.aggregate(pipeline)

    def get_breed_counts(self, match_criteria=None, limit=10):
        """
        Return the top breed counts via a server-side aggregation ($group +
        $sort), instead of loading records into a DataFrame and calling
        value_counts() in Python.

        Parameters:
        match_criteria (dict): optional $match filter applied before grouping
        limit (int): number of top breeds to return

        Returns:
        list: [{'breed': ..., 'count': ...}, ...] sorted by count descending
        """
        pipeline = []
        if match_criteria:
            pipeline.append({'$match': match_criteria})
        pipeline += [
            {'$group': {'_id': '$breed', 'count': {'$sum': 1}}},
            {'$sort': {'count': -1}},
            {'$limit': limit},
            {'$project': {'breed': '$_id', 'count': 1, '_id': 0}}
        ]
        return self.aggregate(pipeline)

    def _migrate_geojson_location(self):
        """
        Backfill a GeoJSON 'location' field ({type: 'Point', coordinates}) from
        location_lat/location_long on documents that don't have one yet, so a
        2dsphere index and true geospatial queries are possible. GeoJSON
        requires [longitude, latitude] order.
        """
        cursor = self.collection.find(
            {
                'location': {'$exists': False},
                'location_lat': {'$exists': True, '$ne': None},
                'location_long': {'$exists': True, '$ne': None}
            },
            {'_id': 1, 'location_lat': 1, 'location_long': 1}
        )
        for doc in cursor:
            self.collection.update_one(
                {'_id': doc['_id']},
                {'$set': {'location': {
                    'type': 'Point',
                    'coordinates': [doc['location_long'], doc['location_lat']]
                }}}
            )

    def create_indexes(self):
        """
        Create the indexes the dashboard's queries rely on:
        - single-field indexes on breed and outcome_type
        - a compound index covering the rescue-type filter (animal_type,
          sex_upon_outcome, breed, age_upon_outcome_in_weeks), ordered by the
          equality/range (ESR) rule so the range field (age) is last
        - a 2dsphere geospatial index on a GeoJSON 'location' field

        Safe to call on every startup: create_index() is a no-op if an
        identical index already exists.
        """
        try:
            self.collection.create_index('breed')
            self.collection.create_index('outcome_type')
            self.collection.create_index(
                [('animal_type', 1), ('sex_upon_outcome', 1),
                 ('breed', 1), ('age_upon_outcome_in_weeks', 1)],
                name='rescue_filter_idx'
            )
            self._migrate_geojson_location()
            self.collection.create_index([('location', '2dsphere')])
            print("Indexes created successfully")
        except Exception as e:
            print(f"Error creating indexes: {e}")
            raise

    def explain_read(self, query):
        """Return find() query-planner output, used to verify index usage."""
        try:
            self._reject_dangerous_operators(query)
            return self.collection.find(query).explain()
        except Exception as e:
            print(f"Error occurred during explain_read operation: {e}")
            return {}

    def explain_aggregate(self, pipeline):
        """Return aggregation query-planner output, used to verify index usage."""
        try:
            self._reject_dangerous_operators(pipeline)
            return self.database.command(
                'aggregate', self.collection.name, pipeline=pipeline, explain=True
            )
        except Exception as e:
            print(f"Error occurred during explain_aggregate operation: {e}")
            return {}